package net.shyshkin.study.docker.candidateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDto {

    private String id;
    private String name;
    private Set<String> skills;

}
