package net.shyshkin.study.docker.jobservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {

    private String id;
    private String description;
    private String company;
    private Set<String> skills;
    private Integer salary;
    private Boolean remote;
    private String hostName;

}
