package au.gov.dxa.monitor.test

import au.gov.dxa.monitor.Application
import au.gov.dxa.monitor.ObservationRepository
import au.gov.dxa.monitor.emit.MetricConverter
import au.gov.dxa.monitor.ingestion.Observation
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
class MetricsTest {


    @Test
    fun can_convert_ordered_observations_into_map_of_ordered_readings() {

        val observations = mutableListOf<Observation>()
        for (i in 0..9) {
            val observation = Observation()
            observation.setTime()
            observation.monitoringValues["i"] = i
            observation.calculatedVariables["e"] = Math.pow(2.0, i.toDouble()).toInt()
            observations.add(observation)
        }

        val metrics = MetricConverter().convert(observations,10)

        Assert.assertTrue(metrics.containsKey("e"))
        Assert.assertEquals(10, metrics["e"]!!.size)
        Assert.assertEquals(1, metrics["e"]!!.first())
        Assert.assertEquals(512, metrics["e"]!!.last())


        Assert.assertTrue(metrics.containsKey("i"))
        Assert.assertEquals(10, metrics["i"]!!.size)
        Assert.assertEquals(0, metrics["i"]!!.first())
        Assert.assertEquals(9, metrics["i"]!!.last())

    }


    @Test
    fun can_leftpad_observations() {

        val observations = mutableListOf<Observation>()
        for (i in 0..5) {
            val observation = Observation()
            observation.setTime()
            observation.calculatedVariables["e"] = Math.pow(2.0, i.toDouble()).toInt()
            observations.add(observation)
        }

        val metrics = MetricConverter().convert(observations,10)

        Assert.assertTrue(metrics.containsKey("e"))
        Assert.assertEquals(10, metrics["e"]!!.size)
        Assert.assertEquals(null, metrics["e"]!!.first())
        Assert.assertEquals(32, metrics["e"]!!.last())

    }


    @Test
    fun can_fill_blanks_in_observations() {


        val observations = mutableListOf<Observation>()
        for (i in 0..9) {
            val observation = Observation()
            observation.setTime()
            observation.calculatedVariables["e"] = Math.pow(2.0, i.toDouble()).toInt()

            if (i % 2 == 0) observation.calculatedVariables["i"] = i

            observations.add(observation)
        }

        val metrics = MetricConverter().convert(observations,10)

        Assert.assertTrue(metrics.containsKey("e"))
        Assert.assertEquals(10, metrics["e"]!!.size)
        Assert.assertEquals(1, metrics["e"]!!.first())
        Assert.assertEquals(512, metrics["e"]!!.last())

        Assert.assertTrue(metrics.containsKey("i"))
        Assert.assertEquals(10, metrics["i"]!!.size)
        Assert.assertEquals(0, metrics["i"]!![0])
        Assert.assertEquals(null, metrics["i"]!![1])
        Assert.assertEquals(2, metrics["i"]!![2])
        Assert.assertEquals(null, metrics["i"]!![3])
        Assert.assertEquals(4, metrics["i"]!![4])
        Assert.assertEquals(null, metrics["i"]!![5])
        Assert.assertEquals(6, metrics["i"]!![6])
        Assert.assertEquals(null, metrics["i"]!![7])
        Assert.assertEquals(8, metrics["i"]!![8])
        Assert.assertEquals(null, metrics["i"]!![9])

    }

}