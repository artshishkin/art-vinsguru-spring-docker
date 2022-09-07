package net.shyshkin.study.docker.candidateservice.service;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.mapper.CandidateMapper;
import net.shyshkin.study.docker.candidateservice.repository.CandidateRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository repository;
    private final CandidateMapper mapper;

    public Flux<CandidateDto> getAllCandidates() {
        return repository.findAll()
                .map(mapper::toDto);
    }

    public Mono<CandidateDto> getCandidateById(String id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    public Mono<CandidateDto> createCandidate(Mono<CandidateDto> newCandidateMono) {
        return newCandidateMono
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }
}
