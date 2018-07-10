package au.gov.dxa.goanna.ingestion

data class Config(val sites:List<SiteDTO>)
data class SiteDTO(val name:String, val url:String, val vars:List<FilterDTO>)
data class FilterDTO(val name:String, val path:String)