package au.gov.dxa.monitor

import au.gov.dxa.monitor.ingestion.Config
import org.springframework.data.mongodb.repository.MongoRepository

interface ConfigRepository : MongoRepository<Config, String> {
}