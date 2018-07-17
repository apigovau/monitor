package au.gov.dxa.monitor.emit

import au.gov.dxa.monitor.ingestion.Observation

class MetricConverter {


    fun convert(fetched:List<Observation>, quantity:Int):Map<String, List<Any?>>{
        val metrics = mutableMapOf<String,MutableList<Any?>>()

        addValuesToMetrics(fetched, metrics, {observation:Observation -> observation.calculatedVariables})
        addValuesToMetrics(fetched, metrics, {observation:Observation -> observation.monitoringValues})

        for(metric in metrics){
            val gap = quantity - metric.value.size
            if(gap == 0) continue
            for(i in 1..gap) metric.value.add(0,null)

            //print("Has metric: `${metric.key}`" + metric.value.toString())

        }




        return metrics
    }

    private fun addValuesToMetrics(fetched: List<Observation>, metrics: MutableMap<String, MutableList<Any?>>, whichMap: (observation:Observation) -> Map<String, Any?>) {
        val observationNames = getObservationNames(fetched, whichMap)
        for (observation in fetched) {
            for (metricName in observationNames) {
                var theValue: Any? = null
                if (whichMap(observation).containsKey(metricName)) {
                    theValue = whichMap(observation)[metricName]
                }
                if (!metrics.containsKey(metricName)) metrics[metricName] = mutableListOf()
                metrics[metricName]!!.add(theValue)
            }
        }
    }

    private fun getObservationNames(fetched: List<Observation>, whichMap: (observation:Observation) -> Map<String, Any?>):List<String> {
        val metricNames = mutableListOf<String>()
        for (observation in fetched) {
            for (value in whichMap(observation)) {
                if (!metricNames.contains(value.key)) metricNames.add(value.key)
            }
        }
        return metricNames
    }


}