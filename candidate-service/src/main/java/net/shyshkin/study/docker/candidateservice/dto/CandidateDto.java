package net.shyshkin.study.docker.candidateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CandidateDto {

    private String id;
    private String name;
    private Set<String> skills;
    private String hostName;

}
