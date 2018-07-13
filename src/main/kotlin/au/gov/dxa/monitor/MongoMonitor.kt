package au.gov.dxa.monitor

import com.mongodb.MongoClient
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MongoMonitor {
    @Value("\${spring.data.mongodb.uri}")
    private lateinit var springDataMongodbUri: String

    @Autowired
    private lateinit var mongoClient: MongoClient

    fun get_mongo_stats():String {
        var dbName = springDataMongodbUri.split("/").last()
        var db = mongoClient.getDatabase(dbName)
        return (db.runCommand(Document("dbStats", 1)).toJson())
    }
}
