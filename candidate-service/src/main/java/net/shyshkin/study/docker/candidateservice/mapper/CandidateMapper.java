package net.shyshkin.study.docker.candidateservice.mapper;

import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.entity.Candidate;
import org.mapstruct.Mapper;

@Mapper
public interface CandidateMapper {

    CandidateDto toDto(Candidate candidate);

    Candidate toEntity(CandidateDto dto);

}
