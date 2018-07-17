package au.gov.dxa.monitor.emit

import au.gov.dxa.monitor.Application
import au.gov.dxa.monitor.StyleDTO
import au.gov.dxa.monitor.WidgetDTO
import org.slf4j.LoggerFactory
import java.text.NumberFormat
import java.util.*
import kotlin.math.max

class BarChartCreator {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    fun create(widget:WidgetDTO, metrics:Map<String, List<Any?>>, styles:List<StyleDTO>):String{

        var style = ""

        if(styles.size > 0){
            style = style + "<style>"
            for(styleDTO in styles){
                style = style + ".${styleDTO.collection} {background-color: ${styleDTO.color} !important; color: ${styleDTO.color};}"
                style = style + ".${styleDTO.collection} .spark {background-color: ${styleDTO.color} !important;}"
            }
            style = style + "</style>\n"
        }

        var head = """
  <div class="bar_chart item" style="grid-column: ${widget.col}/ span ${widget.span};">
    <h3>${widget.title}</h3>
    <span class="direction">⟵ t</span>
    <dl class="chart">
"""
        for(row in widget.bar_chart!!) {

            var latestValue:Any? = 0
            if(metrics.containsKey(row.variable)){
                latestValue = metrics[row.variable]!!.lastOrNull() ?: 0
            }else{
                logger.warn("No recorded metric for '${row.variable}'")
            }

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
        if(metrics.containsKey(row.variable)) {
            var largestValue = -1.0
            if (row.style == "value") for (value in metrics[row.variable]!!) {
                if (value == null) continue
                largestValue = max(largestValue, value.toString().toDouble())
            }

            body = body + """      <dd class="sparkline ${row.collection}">"""
            for (value in metrics[row.variable]!!) {
                var theValue = value
                if (row.style == "value" && value == null) theValue = 0
                if (row.style == "value" && value != null) theValue = value.toString().toDouble() / largestValue * 100
                body = body + """<div title="${value}" class="spark" style="height:${theValue ?: 0}%"></div>"""
            }
            body = body + "</dd>\n"
        }
        else{
            body = body + """      <dd class="sparkline ${row.collection}">"""
            for (i in 1..100) {
                body = body + """<div title="No reading" class="spark" style="height:0%"></div>"""
            }
            body = body + "</dd>\n"
        }
    }


    val tail = """    </dl>
  </div>
"""

        return style + head + body +  tail

    }
}