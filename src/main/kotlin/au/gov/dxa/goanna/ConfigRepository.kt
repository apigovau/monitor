package au.gov.dxa.goanna

import au.gov.dxa.goanna.ingestion.Config
import au.gov.dxa.goanna.ingestion.Observation
import org.springframework.data.mongodb.repository.MongoRepository

interface ConfigRepository : MongoRepository<Config, String> {
}