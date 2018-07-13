package au.gov.dxa.monitor.emit

import au.gov.dxa.monitor.WidgetDTO

class BarChartCreator {


    fun create(widget:WidgetDTO, metrics:Map<String,List<Any?>>):String{
        var head = """
  <div class="bar_chart item" style="grid-column: ${widget.col}/ span ${widget.span};">
    <h3>${widget.title}</h3>
    <dl class="chart">
"""
        for(row in widget.bar_chart!!) {
            val latestValue = metrics[row.variable]!!.last()
            head = head + """      <dd class="value ${row.collection}" style="width:${latestValue}%;"><span class="label">${row.label}</span></dd>""" + "\n"
        }
    var body = """    </dl>
    <dl class="sparklines">""" + "\n"

    for(row in widget.bar_chart!!) {
        body = body + """      <dd class="sparkline">"""
        for (value in metrics[row.variable]!!) {
            body = body + """<div class="spark" style="height:${value ?: 0}%"></div>"""
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