package au.gov.dxa.monitor.emit

import au.gov.dxa.monitor.Application
import au.gov.dxa.monitor.WidgetDTO
import org.slf4j.LoggerFactory
import java.text.NumberFormat
import java.util.*
import kotlin.math.max

class BarChartCreator {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    fun create(widget:WidgetDTO, metrics:Map<String,List<Any?>>):String{
        var head = """
  <div class="bar_chart item" style="grid-column: ${widget.col}/ span ${widget.span};">
    <h3>${widget.title}</h3>
    <dl class="chart">
"""
        for(row in widget.bar_chart!!) {

            if(!metrics.containsKey(row.variable)){
                logger.warn("No recorded metric for '${row.variable}'")
                continue
            }

            val latestValue = metrics[row.variable]!!.lastOrNull() ?: 0
            if(row.style == "percent") {
                head = head + """      <dd class="percentage ${row.collection}" style="width:${latestValue}%;"><span class="label">${row.label}</span></dd>""" + "\n"
            }
            if(row.style == "value") {
                head = head + """      <dd class="value ${row.collection}"><span class="text">${NumberFormat.getNumberInstance(Locale.US).format(latestValue)}</span><span class="label">${row.label}</span></dd>""" + "\n"
            }

        }
    var body = """    </dl>
    <dl class="sparklines">""" + "\n"


    for(row in widget.bar_chart!!) {
        var largestValue = -1.0

        if(row.style=="value") for(value in metrics[row.variable]!!){
            if(value == null) continue
            largestValue = max(largestValue,value.toString().toDouble())
        }

        body = body + """      <dd class="sparkline">"""
        for (value in metrics[row.variable]!!) {
            var theValue = value
            if(row.style=="value" && value == null) theValue = 0
            if(row.style=="value" && value != null) theValue = value.toString().toDouble() / largestValue * 100


            body = body + """<div class="spark" style="height:${theValue ?: 0}%"></div>"""
            //<div class="spark" style="height:100%"></div><div class="spark" style="height:90%"></div><div class="spark" style="height:80%"></div><div class="spark" style="height:70%"></div><div class="spark" style="height:60%"></div><div class="spark" style="height:50%"></div><div class="spark" style="height:40%"></div><div class="spark" style="height:30%"></div><div class="spark" style="height:20%"></div><div class="spark" style="height:10%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div><div class="spark" style="height:0%"></div></dd>
        }
        body = body + "</dd>\n"
    }


    val tail = """    </dl>
  </div>
"""

        return head + body +  tail

    }
}