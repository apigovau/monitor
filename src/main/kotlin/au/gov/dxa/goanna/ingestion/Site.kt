package au.gov.dxa.goanna.ingestion

import javafx.application.Application
import org.slf4j.LoggerFactory
import java.net.URL

class Site(val name:String, val url:String, val vars:List<Filter>) {
    private val logger = LoggerFactory.getLogger(Application::class.java)
    private val test1 = "{\n" +
            "db: \"heroku_0g7226vr\",\n" +
            "collections: 3,\n" +
            "views: 0,\n" +
            "objects: 10,\n" +
            "avgObjSize: 15184,\n" +
            "dataSize: 151840,\n" +
            "storageSize: 282624.0,\n" +
            "numExtents: 4,\n" +
            "indexes: 1,\n" +
            "indexSize: 8176,\n" +
            "fileSize: 16777216,\n" +
            "nsSizeMB: 1,\n" +
            "extentFreeList: {\n" +
            "num: 1,\n" +
            "totalSize: 65536\n" +
            "},\n" +
            "dataFileVersion: {\n" +
            "major: 4,\n" +
            "minor: 22\n" +
            "},\n" +
            "ok: 1\n" +
            "}"

    fun read():List<Filter.Reading>{

        var authKey: String = System.getenv("authKey") ?: ""
        val theUrl = if (url.contains("&")) "$url&authKey=$authKey" else "$url?authKey=$authKey"

        var json = ""
        if(url == "test1") json = test1
        if(json == ""){
            logger.info("Reading json from: $url")
            try {
                json = URL(theUrl).readText()
            }catch(e:Exception){
                logger.info("Couldn't read from: $url. Recording empty observations")
            }
        }

        val readings = mutableListOf<Filter.Reading>()

        for(filter in vars) {
            if (json == "") {
                val reading = Filter.Reading(filter.name, "", "string")
                reading.name = "${name}_${reading.name}"
                readings.add(reading)
            } else {
                val reading = filter.observe(json)
                reading.name = "${name}_${reading.name}"
                readings.add(reading)
            }

        }

        return readings
    }

}