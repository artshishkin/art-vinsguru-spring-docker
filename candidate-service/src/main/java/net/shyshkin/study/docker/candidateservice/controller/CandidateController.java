package net.shyshkin.study.docker.candidateservice.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.service.CandidateService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService service;

    @GetMapping
    public Flux<CandidateDto> getAllCandidates() {
        return service.getAllCandidates();
    }

    @GetMapping("{id}")
    public Mono<CandidateDto> getCandidateById(@PathVariable String id) {
        return service.getCandidateById(id);
    }

    @PostMapping
    public Mono<CandidateDto> createCandidate(@RequestBody Mono<CandidateDto> dtoMono) {
        return service.createCandidate(dtoMono);
    }

}