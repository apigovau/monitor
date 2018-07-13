package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.Config
import com.beust.klaxon.Klaxon
import org.junit.Assert
import org.junit.Test

class BarChartTests {


    val configJson = "{\n" +
            "    \"sites\": [\n" +
            "        {\n" +
            "            \"name\": \"serviceCatalogueRepository\",\n" +
            "            \"url\": \"https://service-catalogue-repository.herokuapp.com/monitor\",\n" +
            "            \"vars\": [\n" +
            "                {\n" +
            "                    \"name\": \"storageSize\",\n" +
            "                    \"path\": \"\$.storageSize\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"fileSize\",\n" +
            "                    \"path\": \"\$.fileSize\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"definitionsCatalogue\",\n" +
            "            \"url\": \"https://definitions.ausdx.io/monitor\",\n" +
            "            \"vars\": [\n" +
            "                {\n" +
            "                    \"name\": \"queryLogTableRows \",\n" +
            "                    \"path\": \"\$.queryLogTableRows \"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"lambdas\": [\n" +
            "        {\n" +
            "            \"name\": \"service Catalogue Repository MongoDB\",\n" +
            "            \"lambda\": \"100 - (serviceCatalogueRepository_fileSize  / 512753664 * 100)\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"definitionCatalogueQueryRepositoryPercent\",\n" +
            "            \"lambda\": \"100 - (definitionsCatalogue_queryLogTableRows  / 10000 * 100)\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"widgets\":[\n" +
            "      {\n" +
            "        \"title\":\"Storage\",\n" +
            "        \"col\":1,\n" +
            "        \"span\":6,\n" +
            "        \"bar_chart\":[\n" +
            "          {\n" +
            "            \"name\":\"Service Catalogue Repository MongoDB\",\n" +
            "            \"collection\":\"svccatrepo\"\n" +
            "          }\n" +
            "          ]\n" +
            "      }\n" +
            "      ]\n" +
            "}"


    @Test
    fun test_bar_chart_config(){

        //println(configJson)

        val config = Klaxon().parse<Config>(configJson)!!
        Assert.assertEquals(1, config.widgets.size)
        Assert.assertEquals("Storage", config.widgets[0].title)
        Assert.assertNotNull(config.widgets[0].bar_chart)
        Assert.assertEquals("Service Catalogue Repository MongoDB", config.widgets[0].bar_chart!![0].name)
        Assert.assertEquals("svccatrepo", config.widgets[0].bar_chart!![0].collection)



    }


    @Test
    fun can_generate_bar_chart_main_percentages(){

        val config = Klaxon().parse<Config>(configJson)!!


    }

}