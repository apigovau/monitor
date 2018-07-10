package au.gov.dxa.goanna.test

import au.gov.dxa.goanna.ingestion.Config
import au.gov.dxa.goanna.ingestion.Observation
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test

class ObservationTests {


    @Test
    fun can_record_observation(){
        val configJson = "{\n" +
                "\t\"sites\": [{\n" +
                "\t\t\"name\": \"serviceCatalogueRepository\",\n" +
                "\t\t\"url\": \"test1\",\n" +
                "\t\t\"vars\": [{\n" +
                "\t\t\t\t\"name\": \"storageSize\",\n" +
                "\t\t\t\t\"path\": \"\$.storageSize\",\n" +
                "\t\t\t\t\"type\": \"float\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\": \"dbName\",\n" +
                "\t\t\t\t\"path\": \"\$.db\",\n" +
                "\t\t\t\t\"type\": \"string\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}]\n" +
                "}"



        val config = Klaxon().parse<Config>(configJson)!!
        val observation = Observation(config)
        Assert.assertEquals(2, observation.values.size)
        Assert.assertTrue( observation.values.containsKey("serviceCatalogueRepository.storageSize"))
        Assert.assertEquals(282624.0, observation.values["serviceCatalogueRepository.storageSize"])
        Assert.assertTrue(observation.values.containsKey("serviceCatalogueRepository.dbName"))
        Assert.assertEquals("heroku_0g7226vr", observation.values["serviceCatalogueRepository.dbName"])


    }

}