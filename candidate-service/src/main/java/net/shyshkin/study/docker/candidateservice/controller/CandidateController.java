package net.shyshkin.study.docker.candidateservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDetailsDto;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.service.CandidateService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService service;

    @GetMapping
    public Flux<CandidateDto> getAllCandidates() {
        log.info("Getting all candidates");
        return service.getAllCandidates();
    }

    @GetMapping("{id}")
    public Mono<CandidateDetailsDto> getCandidateById(@PathVariable String id) {
        log.debug("Getting candidate by id: {}", id);
        return service.getCandidateById(id);
    }

    @PostMapping
    public Mono<CandidateDto> createCandidate(@RequestBody Mono<CandidateDto> dtoMono) {
        return service.createCandidate(dtoMono)
                .doOnNext(dto -> log.debug("Created new candidate: {}", dto));
    }

}
