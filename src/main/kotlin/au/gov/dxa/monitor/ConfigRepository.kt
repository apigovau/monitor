package au.gov.dxa.monitor

import org.springframework.data.mongodb.repository.MongoRepository

interface ConfigRepository : MongoRepository<Config, String> {
}