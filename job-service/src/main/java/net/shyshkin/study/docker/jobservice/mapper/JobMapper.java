package net.shyshkin.study.docker.jobservice.mapper;

import net.shyshkin.study.docker.jobservice.dto.JobDto;
import net.shyshkin.study.docker.jobservice.entity.Job;
import org.mapstruct.Mapper;

@Mapper
public interface JobMapper {

    JobDto toDto(Job job);

    Job toEntity(JobDto dto);

}
