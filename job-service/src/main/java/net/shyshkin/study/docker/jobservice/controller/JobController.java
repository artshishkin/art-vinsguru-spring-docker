package net.shyshkin.study.docker.jobservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.docker.jobservice.dto.JobDto;
import net.shyshkin.study.docker.jobservice.service.JobService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public Flux<JobDto> getAllJobs() {
        log.info("Getting all the jobs");
        return jobService.allJobs();
    }

    @GetMapping(params = {"skills"})
    public Flux<JobDto> getJobsBySkills(@RequestParam("skills") Set<String> skills) {
        log.debug("Getting jobs by skills");
        return jobService.jobsBySkills(skills);
    }

    @GetMapping("{id}")
    public Mono<JobDto> getJobById(@PathVariable String id) {
        log.debug("Getting job by id");
        return jobService.getJob(id);
    }

    @PostMapping
    public Mono<JobDto> createNewJob(@RequestBody Mono<JobDto> jobDtoMono) {
        return jobService.createNewJob(jobDtoMono)
                .doOnNext(dto -> log.debug("Created new job {}", dto));
    }

    @PutMapping("{id}")
    public Mono<JobDto> updateJob(@PathVariable String id, @RequestBody Mono<JobDto> jobDtoMono) {
        log.debug("Start updating job with id: {}", id);
        return jobService.updateJob(id, jobDtoMono)
                .doOnNext(dto -> log.debug("Job updated: {}", dto));
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable String id) {
        log.warn("Deleting job with id: {}", id);
        return jobService.delete(id);
    }

}
