package net.shyshkin.study.docker.jobservice.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.docker.jobservice.dto.JobDto;
import net.shyshkin.study.docker.jobservice.service.JobService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public Flux<JobDto> getAllJobs() {
        return jobService.allJobs();
    }

    @GetMapping(params = {"skills"})
    public Flux<JobDto> getJobsBySkills(@RequestParam("skills") Set<String> skills) {
        return jobService.jobsBySkills(skills);
    }

    @GetMapping("{id}")
    public Mono<JobDto> getJobById(@PathVariable String id) {
        return jobService.getJob(id);
    }

    @PostMapping
    public Mono<JobDto> createNewJob(@RequestBody Mono<JobDto> jobDtoMono) {
        return jobService.createNewJob(jobDtoMono);
    }

    @PutMapping("{id}")
    public Mono<JobDto> updateJob(@PathVariable String id, @RequestBody Mono<JobDto> jobDtoMono) {
        return jobService.updateJob(id, jobDtoMono);
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return jobService.delete(id);
    }

}
