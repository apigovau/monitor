package au.gov.dxa.goanna.ingestion

import com.jayway.jsonpath.JsonPath
import javafx.application.Application
import org.slf4j.LoggerFactory


class Filter(val name:String, val path:String, val type:Type) {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    enum class Type {
        int, string, float
    }

    private var _success = true
    private var _stringValue = ""

    data class Reading(var name:String, val value:String, val type:String)
    fun observe(json:String):Reading{
        try {
            if (type == Type.int) _stringValue = JsonPath.read<Integer>(json, path).toString()
            if (type == Type.float) _stringValue = JsonPath.read<Double>(json, path).toString()
            if (type == Type.string) _stringValue = JsonPath.read<String>(json, path)
        }catch(e:Exception){
            _success = false
            logger.warn("Something went wrong reading filter[$name] : ${e.message}")
        }

        return Reading(name, _stringValue, type.toString())
    }

}