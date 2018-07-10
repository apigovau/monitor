package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.Application
import au.gov.dxa.monitor.ObservationRepository
import au.gov.dxa.monitor.ingestion.Config
import au.gov.dxa.monitor.ingestion.Observation
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes=[Application::class])
@Import(value = FakeMongo::class)
class ObservationRepositoryTest {

    @Autowired
    private lateinit var repository: ObservationRepository

    @Test
    fun can_save_service(){

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
        val observation = Observation()
        observation.observe(config)

        repository.save(observation)

        val ob = repository.findAll().first()


        Assert.assertEquals(2, ob.values.size)
        Assert.assertTrue( ob.values.containsKey("serviceCatalogueRepository_storageSize"))
        Assert.assertEquals(282624.0, ob.values["serviceCatalogueRepository_storageSize"])
        Assert.assertTrue(ob.values.containsKey("serviceCatalogueRepository_dbName"))
        Assert.assertEquals("heroku_0g7226vr", ob.values["serviceCatalogueRepository_dbName"])


    }


}