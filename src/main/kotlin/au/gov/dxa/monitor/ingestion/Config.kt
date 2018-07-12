package au.gov.dxa.monitor.ingestion

data class Config(val sites:List<SiteDTO>, val lambdas:List<LambdaDTO> = listOf())
data class SiteDTO(val name:String, val url:String, val vars:List<FilterDTO>)
data class FilterDTO(val name:String, val path:String)
data class LambdaDTO(val name:String, val lambda:String)