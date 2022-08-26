package net.shyshkin.study.docker.jobservice.repository;

import net.shyshkin.study.docker.jobservice.entity.Job;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface JobRepository extends ReactiveMongoRepository<Job, String> {

    Flux<Job> findBySkillsIn(Set<String> skills);
}
