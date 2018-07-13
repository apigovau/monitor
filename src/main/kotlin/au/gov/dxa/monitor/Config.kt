package au.gov.dxa.monitor

data class Config(val sites:List<SiteDTO>, val lambdas:List<LambdaDTO> = listOf(), val widgets:List<WidgetDTO> = listOf())
data class SiteDTO(val name:String, val url:String, val vars:List<FilterDTO>)
data class FilterDTO(val name:String, val path:String)
data class LambdaDTO(val name:String, val lambda:String)
data class WidgetDTO(val title:String, val col:Int, val span:Int, val bar_chart:List<BarChartDTO>? = null)
data class BarChartDTO(val label:String, val variable:String, val collection:String = "")