package net.shyshkin.study.docker.candidateservice.mapper;

import net.shyshkin.study.docker.candidateservice.dto.CandidateDetailsDto;
import net.shyshkin.study.docker.candidateservice.dto.CandidateDto;
import net.shyshkin.study.docker.candidateservice.entity.Candidate;
import net.shyshkin.study.docker.candidateservice.util.AppUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {AppUtil.class})
public interface CandidateMapper {

    @Mapping(target = "hostName",expression = "java( AppUtil.getHostName() )")
    CandidateDto toDto(Candidate candidate);

    @Mapping(target = "hostName",expression = "java( AppUtil.getHostName() )")
    CandidateDetailsDto toDetailsDto(Candidate candidate);

    Candidate toEntity(CandidateDto dto);

}
