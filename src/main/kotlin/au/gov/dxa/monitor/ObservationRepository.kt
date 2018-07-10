package au.gov.dxa.monitor

import au.gov.dxa.monitor.ingestion.Observation
import org.springframework.data.mongodb.repository.MongoRepository

interface ObservationRepository : MongoRepository<Observation, String> {
}