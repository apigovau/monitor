package au.gov.dxa.goanna.ingestion

import java.time.LocalDateTime


class Observation{


    var values = mutableMapOf<String,Any>()
    var time :String = ""

    fun observe(config:Config){
        if(time == "") time = LocalDateTime.now().toString()
        for(siteDTO in config.sites){
            val site = Site(siteDTO.name, siteDTO.url, siteDTO.vars.map{ it-> Filter(it.name, it.path)})
            for(reading in site.read()) {
                if (reading.type == "string") values[reading.name] =  reading.value
                if (reading.type == "int") values[reading.name] = reading.value.toInt()
                if (reading.type == "float") values[reading.name] = reading.value.toDouble()
            }
        }
    }
}
