package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.Application
import au.gov.dxa.monitor.ObservationRepository
import au.gov.dxa.monitor.Config
import au.gov.dxa.monitor.emit.MetricConverter
import au.gov.dxa.monitor.ingestion.Observation
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort
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


        Assert.assertEquals(2, ob.monitoringValues.size)
        Assert.assertTrue( ob.monitoringValues.containsKey("serviceCatalogueRepository_storageSize"))
        Assert.assertEquals(282624.0, ob.monitoringValues["serviceCatalogueRepository_storageSize"])
        Assert.assertTrue(ob.monitoringValues.containsKey("serviceCatalogueRepository_dbName"))
        Assert.assertEquals("heroku_0g7226vr", ob.monitoringValues["serviceCatalogueRepository_dbName"])


    }


    @Test
    fun can_get_and_put_observation_with_lambdas(){


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
                "\t}],\n" +
                "\t\"lambdas\":[\n" +
                "\t  {\n" +
                "\t    \"name\":\"serviceCatalogueRepositoryStoragePercent\",\n" +
                "\t    \"lambda\":\"serviceCatalogueRepository_storageSize / 512753664 * 100\"\n" +
                "\t  },\n" +
                "\t  {\n" +
                "\t    \"name\":\"serviceCatalogueRepositoryDbNameCaps\",\n" +
                "\t    \"lambda\":\"serviceCatalogueRepository_dbName.toUpperCase()\"\n" +
                "\t  }\n" +
                "\t  ]\n" +
                "\t\n" +
                "}"


        val config = Klaxon().parse<Config>(configJson)!!

        Assert.assertEquals(2, config.lambdas.size)
        Assert.assertEquals(config.lambdas[0].name, "serviceCatalogueRepositoryStoragePercent")
        Assert.assertEquals(config.lambdas[0].lambda, "serviceCatalogueRepository_storageSize / 512753664 * 100")
        val observation = Observation()
        observation.observe(config)

        repository.save(observation)

        val ob = repository.findAll().first()

        Assert.assertEquals(2, ob.monitoringValues.size)
        Assert.assertTrue( ob.monitoringValues.containsKey("serviceCatalogueRepository_storageSize"))
        Assert.assertEquals(282624.0, ob.monitoringValues["serviceCatalogueRepository_storageSize"])
        Assert.assertTrue(ob.monitoringValues.containsKey("serviceCatalogueRepository_dbName"))
        Assert.assertEquals("heroku_0g7226vr", ob.monitoringValues["serviceCatalogueRepository_dbName"])

        val gotCalcs = ob.calculatedVariables
        Assert.assertEquals(2, gotCalcs.size)
        Assert.assertTrue(gotCalcs.containsKey("serviceCatalogueRepositoryStoragePercent"))
        Assert.assertEquals(gotCalcs["serviceCatalogueRepositoryStoragePercent"],  0.05511886503067484)
        Assert.assertTrue(gotCalcs.containsKey("serviceCatalogueRepositoryDbNameCaps"))
        Assert.assertEquals(gotCalcs["serviceCatalogueRepositoryDbNameCaps"], "heroku_0g7226vr".toUpperCase())


    }

}