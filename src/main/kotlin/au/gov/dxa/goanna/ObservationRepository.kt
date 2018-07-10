package au.gov.dxa.goanna

import au.gov.dxa.goanna.ingestion.Observation
import org.springframework.data.mongodb.repository.MongoRepository

interface ObservationRepository : MongoRepository<Observation, String> {
}