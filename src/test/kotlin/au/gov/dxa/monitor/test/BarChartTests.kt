package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.Config
import au.gov.dxa.monitor.emit.BarChartCreator
import au.gov.dxa.monitor.emit.MetricConverter
import au.gov.dxa.monitor.ingestion.Observation
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
            "            \"name\": \"serviceCatalogueRepositoryMongoDB\",\n" +
            "            \"lambda\": \"100 - (serviceCatalogueRepository_fileSize  / 512753664 * 100)\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"definitionCatalogueQueryPostgreSQL\",\n" +
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
            "            \"label\":\"Service Catalogue Repository MongoDB\",\n" +
            "            \"style\":\"percent\",\n" +
            "            \"variable\":\"serviceCatalogueRepositoryMongoDB\",\n" +
            "            \"collection\":\"service_catalogue_repository\"\n" +
            "          }\n" +
            "          ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"title\":\"Values\",\n" +
            "        \"col\":1,\n" +
            "        \"span\":6,\n" +
            "        \"bar_chart\":[\n" +
            "          {\n" +
            "            \"label\":\"Service Catalogue Repository MongoDB\",\n" +
            "            \"style\":\"value\",\n" +
            "            \"variable\":\"serviceCatalogueRepositoryMongoDB\",\n" +
            "            \"collection\":\"service_catalogue_repository\"\n" +
            "          }\n" +
            "          ]\n" +
            "      }\n" +
            "      ]\n" +
            "}"


    @Test
    fun test_bar_chart_config(){

        //println(configJson)

        val config = Klaxon().parse<Config>(configJson)!!
        Assert.assertEquals(2, config.widgets.size)
        Assert.assertEquals("Storage", config.widgets[0].title)
        Assert.assertNotNull(config.widgets[0].bar_chart)
        Assert.assertEquals("Service Catalogue Repository MongoDB", config.widgets[0].bar_chart!![0].label)
        Assert.assertEquals("serviceCatalogueRepositoryMongoDB", config.widgets[0].bar_chart!![0].variable)
        Assert.assertEquals("service_catalogue_repository", config.widgets[0].bar_chart!![0].collection)



    }


    @Test
    fun can_generate_bar_chart_main_percentages(){
        val config = Klaxon().parse<Config>(configJson)!!
        val metrics = mutableMapOf<String,List<Any?>>()
        metrics["serviceCatalogueRepositoryMongoDB"] = listOf(null, null, null, null, null, null, null, null, null, 0,10,20,30,40,50,60,70,80,90,100)
        //metrics["Definition Catalogue Query PostgreSQL"] = listOf(0,10,20,30,40,50,60,70,80,90,null, null, null, null, null, null, null, null, null)

        val bcc = BarChartCreator()
        val bc = bcc.create(config.widgets[0], metrics, config.styles)


        val expected =
                """
  <div class="bar_chart item" style="grid-column: 1/ span 6;">
    <h3>Storage</h3>
    <span class="direction">⟵ t</span>
    <dl class="chart">
      <dd class="percentage service_catalogue_repository" style="width:100%;"><span class="label">Service Catalogue Repository MongoDB</span></dd>
    </dl>
    <dl class="sparklines">
      <dd class="sparkline service_catalogue_repository"><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="0" class="spark" style="height:0%"></div><div title="10" class="spark" style="height:10%"></div><div title="20" class="spark" style="height:20%"></div><div title="30" class="spark" style="height:30%"></div><div title="40" class="spark" style="height:40%"></div><div title="50" class="spark" style="height:50%"></div><div title="60" class="spark" style="height:60%"></div><div title="70" class="spark" style="height:70%"></div><div title="80" class="spark" style="height:80%"></div><div title="90" class="spark" style="height:90%"></div><div title="100" class="spark" style="height:100%"></div></dd>
    </dl>
  </div>
"""


        Assert.assertEquals(expected, bc)
    }



    @Test
    fun can_generate_bar_chart_main_values(){
        val config = Klaxon().parse<Config>(configJson)!!
        val metrics = mutableMapOf<String,List<Any?>>()
        metrics["serviceCatalogueRepositoryMongoDB"] = listOf(null, null, null, null, null, null, null, null, null, 0,100,200,300,400,500,600,700,800,900,1000)

        val bcc = BarChartCreator()
        val bc = bcc.create(config.widgets[1], metrics, config.styles)


        val expected =
                """
  <div class="bar_chart item" style="grid-column: 1/ span 6;">
    <h3>Values</h3>
    <span class="direction">⟵ t</span>
    <dl class="chart">
      <dd class="value service_catalogue_repository"><span class="text">1,000</span><span class="label">Service Catalogue Repository MongoDB</span></dd>
    </dl>
    <dl class="sparklines">
      <dd class="sparkline service_catalogue_repository"><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="null" class="spark" style="height:0%"></div><div title="0" class="spark" style="height:0.0%"></div><div title="100" class="spark" style="height:10.0%"></div><div title="200" class="spark" style="height:20.0%"></div><div title="300" class="spark" style="height:30.0%"></div><div title="400" class="spark" style="height:40.0%"></div><div title="500" class="spark" style="height:50.0%"></div><div title="600" class="spark" style="height:60.0%"></div><div title="700" class="spark" style="height:70.0%"></div><div title="800" class="spark" style="height:80.0%"></div><div title="900" class="spark" style="height:90.0%"></div><div title="1000" class="spark" style="height:100.0%"></div></dd>
    </dl>
  </div>
"""


        Assert.assertEquals(expected, bc)
    }

}