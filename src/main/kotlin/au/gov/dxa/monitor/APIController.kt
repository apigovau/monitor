package au.gov.dxa.monitor

import au.gov.dxa.monitor.emit.BarChartCreator
import au.gov.dxa.monitor.emit.MetricConverter
import au.gov.dxa.monitor.ingestion.Observation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*

@RestController
class APIController {

    @Autowired
    private lateinit var configRepository: ConfigRepository

    @Autowired
    private lateinit var observationRepository: ObservationRepository

    @Autowired lateinit var monitor: MongoMonitor

    @Scheduled(fixedRate = 300000)
    @GetMapping("/observe")
    fun observe(){
        val config = configRepository.findAll().firstOrNull()
        if(config == null) return
        val observation = Observation()
        observation.observe(config)
        observationRepository.save(observation)
    }

    @CrossOrigin
    @GetMapping("/monitor")
    fun test_db_stats(@RequestParam authKey:String):String{
        var authKeyEnv: String = System.getenv("authKey") ?: ""
        if(authKey != authKeyEnv) throw UnauthorisedToAccessMonitoring()
        return monitor.get_mongo_stats()
    }

    //@CrossOrigin
    //@GetMapping("/observations")
    private fun observations():List<Observation>{
        return observationRepository.findAll(Sort(Sort.Direction.ASC, "time"))
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    class UnauthorisedToAccessMonitoring() : RuntimeException()

    @GetMapping("/")
    fun index(@RequestParam authKey:String):String{
        var authKeyEnv: String = System.getenv("authKey") ?: ""
        if(authKey != authKeyEnv) throw UnauthorisedToAccessMonitoring()

        val head = """
<!DOCTYPE html>
<html lang="en" >
<head>
  <meta http-equiv="refresh" content="250">
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body translate="no" >
<div class="metric_container">
"""



        val tail = """
</div>
</body>
</html>
"""

        var content = ""

        val config = configRepository.findAll().first()
        val observations = observationRepository.findAll(Sort(Sort.Direction.ASC, "time")).takeLast(20)
        val metrics = MetricConverter().convert(observations,20)
        val bcc = BarChartCreator()


        for(widget in config.widgets){
            if(widget.bar_chart != null){
                content = content + bcc.create(widget, metrics) + "\n"
            }
        }

        return head + content + tail
    }
}

