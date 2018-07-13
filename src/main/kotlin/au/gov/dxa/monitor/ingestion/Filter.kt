package au.gov.dxa.monitor.ingestion

import au.gov.dxa.monitor.Application
import com.jayway.jsonpath.JsonPath
import org.slf4j.LoggerFactory


class Filter(var name:String, var path:String) {

    @Transient
    private val logger = LoggerFactory.getLogger(Application::class.java)

    data class Reading(var name:String, val value:String, val type:String)
    fun observe(json:String): Reading {
        try { return Reading(name, JsonPath.read<Integer>(json, path).toString(), "int")
        }catch(e:Exception){ }

        try { return Reading(name, String.format("%.2f", JsonPath.read<Double>(json, path)), "float")
        }catch(e:Exception){ }

        try { return Reading(name, JsonPath.read<String>(json, path).toString(), "string")
        }catch(e:Exception){ }

        return Reading(name, "", "string")
    }

}