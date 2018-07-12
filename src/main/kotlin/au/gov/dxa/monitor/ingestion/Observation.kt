package au.gov.dxa.monitor.ingestion

import au.gov.dxa.monitor.emit.LambdaRunner
import java.time.LocalDateTime


class Observation{

    var calculatedVariables = mapOf<String,Any>()
    var monitoringValues = mutableMapOf<String,Any>()
    var time :String = ""

    fun observe(config:Config){
        if(time == "") time = LocalDateTime.now().toString()
        for(siteDTO in config.sites){
            val site = Site(siteDTO.name, siteDTO.url, siteDTO.vars.map{ it-> Filter(it.name, it.path)})
            for(reading in site.read()) {
                if (reading.type == "string") monitoringValues[reading.name] =  reading.value
                if (reading.type == "int") monitoringValues[reading.name] = reading.value.toInt()
                if (reading.type == "float") monitoringValues[reading.name] = reading.value.toDouble()
            }
        }

        val lambdaRunner = LambdaRunner(this)
        calculatedVariables = lambdaRunner.evalLambdas(config)
    }

}
