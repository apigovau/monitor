package au.gov.dxa.monitor

import au.gov.dxa.monitor.ingestion.Observation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class APIController {

    @Autowired
    private lateinit var configRepository: ConfigRepository

    @Autowired
    private lateinit var observationRepository: ObservationRepository

    @CrossOrigin
    @GetMapping("/observe")
    fun observe():Observation{
        val config = configRepository.findAll().first()
        val observation = Observation()
        observation.observe(config)
        observationRepository.save(observation)
        return observation
    }

    @CrossOrigin
    @GetMapping("/observations")
    fun observations():List<Observation>{
        return observationRepository.findAll(Sort(Sort.Direction.DESC, "time"))
    }
}

