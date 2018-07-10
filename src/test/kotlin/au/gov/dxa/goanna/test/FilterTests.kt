package au.gov.dxa.goanna.test

import au.gov.dxa.goanna.ingestion.Filter
import com.jayway.jsonpath.JsonPath
import org.junit.Assert
import org.junit.Test

class FilterTests {


    @Test
    fun can_get_json_value_for_path(){

        val json = "{\n" +
                "db: \"heroku_0g7226vr\",\n" +
                "collections: 3,\n" +
                "views: 0,\n" +
                "objects: 10,\n" +
                "avgObjSize: 15184,\n" +
                "dataSize: 151840,\n" +
                "storageSize: 282624,\n" +
                "numExtents: 4,\n" +
                "indexes: 1,\n" +
                "indexSize: 8176,\n" +
                "fileSize: 16777216,\n" +
                "nsSizeMB: 1,\n" +
                "extentFreeList: {\n" +
                "num: 1,\n" +
                "totalSize: 65536\n" +
                "},\n" +
                "dataFileVersion: {\n" +
                "major: 4,\n" +
                "minor: 22\n" +
                "},\n" +
                "ok: 1\n" +
                "}"

        val query = "$.storageSize"

        val storageSize = JsonPath.read<Integer>(json, query)
        Assert.assertEquals(282624, storageSize)
    }


    @Test
    fun can_get_json_value_for_path_from_observable(){

        val json = "{\n" +
                "db: \"heroku_0g7226vr\",\n" +
                "collections: 3,\n" +
                "views: 0,\n" +
                "objects: 10,\n" +
                "avgObjSize: 15184,\n" +
                "dataSize: 151840,\n" +
                "storageSize: 282624,\n" +
                "numExtents: 4,\n" +
                "indexes: 1,\n" +
                "indexSize: 8176,\n" +
                "fileSize: 16777216,\n" +
                "nsSizeMB: 1,\n" +
                "extentFreeList: {\n" +
                "num: 1,\n" +
                "totalSize: 65536\n" +
                "},\n" +
                "dataFileVersion: {\n" +
                "major: 4,\n" +
                "minor: 22\n" +
                "},\n" +
                "ok: 1\n" +
                "}"



        val storageSize = Filter("storageSize", "$.storageSize", Filter.Type.int)
        val storageSizeResult = storageSize.observe(json)
        Assert.assertEquals("282624", storageSizeResult.value)


        val name = Filter("name", "$.db", Filter.Type.string)
        val nameResult = name.observe(json)
        Assert.assertEquals("heroku_0g7226vr", nameResult.value)


        val noReading = Filter("name", "$.noReading", Filter.Type.string)
        val noReadingResult = noReading.observe(json)
        Assert.assertEquals("", noReadingResult.value)


        val noReadingInt = Filter("name", "$.noReading", Filter.Type.int)
        val noReadingIntResult = noReadingInt.observe(json)
        Assert.assertEquals("", noReadingIntResult.value)

    }

}