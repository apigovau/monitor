package au.gov.dxa.goanna.test

import au.gov.dxa.goanna.ingestion.Filter
import au.gov.dxa.goanna.ingestion.Site
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test

class SiteTests {

    @Test
    fun can_configure_site(){

        val filters = listOf<Filter>(Filter("storageSize", "$.storageSize", Filter.Type.float), Filter("dbName","$.db",Filter.Type.string))
        val site = Site("serviceCatalogueRepository", "test1", filters)

        val readings = site.read()
        Assert.assertEquals(2, readings.size)

        Assert.assertEquals("serviceCatalogueRepository.storageSize", readings[0].name)
        Assert.assertEquals("282624.0", readings[0].value)
        Assert.assertEquals("float", readings[0].type)

        Assert.assertEquals("serviceCatalogueRepository.dbName", readings[1].name)
        Assert.assertEquals("heroku_0g7226vr", readings[1].value)
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
        Assert.assertEquals( 2, site.vars.size)

        val readings = site.read()
        Assert.assertEquals(2, readings.size)

        Assert.assertEquals("serviceCatalogueRepository.storageSize", readings[0].name)
        Assert.assertEquals("282624.0", readings[0].value)
        Assert.assertEquals("float", readings[0].type)

        Assert.assertEquals("serviceCatalogueRepository.dbName", readings[1].name)
        Assert.assertEquals("heroku_0g7226vr", readings[1].value)
        Assert.assertEquals("string", readings[1].type)

    }


}