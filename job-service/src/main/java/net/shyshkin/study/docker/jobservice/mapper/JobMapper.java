package net.shyshkin.study.docker.jobservice.mapper;

import net.shyshkin.study.docker.jobservice.dto.JobDto;
import net.shyshkin.study.docker.jobservice.entity.Job;
import net.shyshkin.study.docker.jobservice.util.AppUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {AppUtil.class})
public interface JobMapper {

    @Mapping(target = "hostName", expression = "java( AppUtil.getHostName() )")
    JobDto toDto(Job job);

    Job toEntity(JobDto dto);

}
