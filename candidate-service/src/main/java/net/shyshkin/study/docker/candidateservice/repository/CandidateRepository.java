package net.shyshkin.study.docker.candidateservice.repository;

import net.shyshkin.study.docker.candidateservice.entity.Candidate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CandidateRepository extends ReactiveMongoRepository<Candidate, String> {
}
