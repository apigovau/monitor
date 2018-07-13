package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.Application
import au.gov.dxa.monitor.ConfigRepository
import au.gov.dxa.monitor.Config
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
class ConfigRepositoryTest {

    @Autowired
    private lateinit var repository: ConfigRepository

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
        repository.save(config)
        val conf = repository.findAll().first()

        Assert.assertEquals(1, conf.sites.size)
        Assert.assertEquals("test1", conf.sites[0].url)
        Assert.assertEquals("serviceCatalogueRepository", conf.sites[0].name)
        Assert.assertEquals(2, conf.sites[0].vars.size)


    }


}