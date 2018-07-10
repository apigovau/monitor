package au.gov.dxa.goanna.ingestion

import com.beust.klaxon.Json
import java.time.LocalDateTime

data class Config(val sites:List<Site>)
class Observation(@Json(ignored = true) val config:Config) {

    val values = mutableMapOf<String,Any>()
    val time = LocalDateTime.now().toString()

    init{
        for(site in config.sites){
            for(reading in site.read()){
                if(reading.type == "string") values.put(reading.name, reading.value )
                if(reading.type == "int") values.put(reading.name, reading.value.toInt() )
                if(reading.type == "float") values.put(reading.name, reading.value.toDouble() )
            }
        }
    }

}
