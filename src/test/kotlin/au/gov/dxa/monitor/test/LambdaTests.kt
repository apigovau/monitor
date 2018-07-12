package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.emit.LambdaRunner
import au.gov.dxa.monitor.ingestion.Config
import au.gov.dxa.monitor.ingestion.Observation
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test
import javax.script.ScriptEngineManager

class LambdaTests {



    @Test
    fun can_build_global_vars_from_observation_results() {

        var observation = Observation()
        observation.monitoringValues["serviceCatalogueRepository_storageSize"] = 282624.0
        observation.monitoringValues["serviceCatalogueRepository_dbName"] = "heroku_0g7226vr"


        Assert.assertTrue(observation.monitoringValues.containsKey("serviceCatalogueRepository_storageSize"))
        Assert.assertEquals(282624.0, observation.monitoringValues["serviceCatalogueRepository_storageSize"])
        Assert.assertTrue(observation.monitoringValues.containsKey("serviceCatalogueRepository_dbName"))
        Assert.assertEquals("heroku_0g7226vr", observation.monitoringValues["serviceCatalogueRepository_dbName"])


        var expected = """var serviceCatalogueRepository_storageSize = 282624.0;
var serviceCatalogueRepository_dbName = "heroku_0g7226vr";
"""

        var lf = LambdaRunner(observation)
        Assert.assertEquals(expected, lf.globalVars)
    }

    @Test
    fun can_execute_lambda_and_return_value(){

        var observation = Observation()
        observation.monitoringValues["serviceCatalogueRepository_storageSize"] = 512753664 / 2
        observation.monitoringValues["serviceCatalogueRepository_dbName"] = "heroku_0g7226vr"


        var lf = LambdaRunner(observation)
        var lambda = "serviceCatalogueRepository_storageSize / 512753664 * 100"
        var expected = 50.0

        Assert.assertEquals(expected, lf.eval(lambda))
    }


    @Test
    fun can_get_lambda_config_and_return_results(){


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
        val calcs = observation.calculatedVariables
        Assert.assertEquals(2, calcs.size)
        Assert.assertTrue(calcs.containsKey("serviceCatalogueRepositoryStoragePercent"))
        Assert.assertEquals(calcs["serviceCatalogueRepositoryStoragePercent"],  0.05511886503067484)
        Assert.assertTrue(calcs.containsKey("serviceCatalogueRepositoryDbNameCaps"))
        Assert.assertEquals(calcs["serviceCatalogueRepositoryDbNameCaps"], "heroku_0g7226vr".toUpperCase())


    }



    @Test
    fun test_can_get_return_value_from_nashorn(){
        val engine = ScriptEngineManager().getEngineByName("nashorn")
        var x = engine.eval("var a_b = 13; a_b * 2;")
        Assert.assertEquals(26.0, x)
    }

}