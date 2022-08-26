package net.shyshkin.study.docker.jobservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Job {

    @Id
    private String id;
    private String description;
    private String company;
    private Set<String> skills;
    private Integer salary;
    private Boolean remote;

}
