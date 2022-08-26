package net.shyshkin.study.docker.jobservice.service;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.docker.jobservice.dto.JobDto;
import net.shyshkin.study.docker.jobservice.mapper.JobMapper;
import net.shyshkin.study.docker.jobservice.repository.JobRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository repository;
    private final JobMapper mapper;

    public Flux<JobDto> allJobs() {
        return repository
                .findAll()
                .map(mapper::toDto);
    }

    public Flux<JobDto> jobsBySkills(Set<String> skills) {
        return repository
                .findBySkillsIn(skills)
                .map(mapper::toDto);
    }

    public Mono<JobDto> getJob(String id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    public Mono<JobDto> createNewJob(Mono<JobDto> jobDto) {
        return jobDto
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<JobDto> updateJob(String id, Mono<JobDto> newJobDto) {
        return repository.findById(id)
                .flatMap(job -> newJobDto)
                .doOnNext(dto -> dto.setId(id))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

}
