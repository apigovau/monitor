package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.ingestion.Filter
import au.gov.dxa.monitor.ingestion.Site
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test

class SiteTests {

    @Test
    fun can_configure_site(){

        val filters = listOf<Filter>(Filter("storageSize", "$.storageSize"), Filter("dbName","$.db"))
        val site = Site("serviceCatalogueRepository", "test1", filters)

        val readings = site.read()
        Assert.assertEquals(2, readings.size)

        Assert.assertEquals("serviceCatalogueRepository_storageSize", readings[0].name)
        Assert.assertEquals("282624.00", readings[0].value)
        Assert.assertEquals("float", readings[0].type)

        Assert.assertEquals("serviceCatalogueRepository_dbName", readings[1].name)
        Assert.assertEquals("heroku_0g7226vr", readings[1].value)
        Assert.assertEquals("string", readings[1].type)

    }

    @Test
    fun will_get_empty_readings_if_site_fails(){

        val filters = listOf<Filter>(Filter("storageSize", "$.storageSize"), Filter("dbName","$.db"))
        val site = Site("serviceCatalogueRepository", "http://test12", filters)

        val readings = site.read()
        Assert.assertEquals(2, readings.size)

        Assert.assertEquals("serviceCatalogueRepository_storageSize", readings[0].name)
        Assert.assertEquals("", readings[0].value)
        Assert.assertEquals("string", readings[0].type)

        Assert.assertEquals("serviceCatalogueRepository_dbName", readings[1].name)
        Assert.assertEquals("", readings[1].value)
        Assert.assertEquals("string", readings[1].type)

    }

    @Test
    fun can_get_site_conf_from_json(){

        val siteConf = "{\n" +
                "\t\"name\": \"serviceCatalogueRepository\",\n" +
                "\t\"url\": \"test1\",\n" +
                "\t\"vars\": [{\n" +
                "\t\t\t\"name\": \"storageSize\",\n" +
                "\t\t\t\"path\": \"\$.storageSize\",\n" +
                "\t\t\t\"type\": \"float\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"dbName\",\n" +
                "\t\t\t\"path\": \"\$.db\",\n" +
                "\t\t\t\"type\": \"string\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}"

        val site = Klaxon().parse<Site>(siteConf)!!

        Assert.assertEquals("serviceCatalogueRepository", site.name)
        Assert.assertEquals( 2, site.vars!!.size)

        val readings = site.read()
        Assert.assertEquals(2, readings.size)

        Assert.assertEquals("serviceCatalogueRepository_storageSize", readings[0].name)
        Assert.assertEquals("282624.00", readings[0].value)
        Assert.assertEquals("float", readings[0].type)

        Assert.assertEquals("serviceCatalogueRepository_dbName", readings[1].name)
        Assert.assertEquals("heroku_0g7226vr", readings[1].value)
        Assert.assertEquals("string", readings[1].type)

    }


}