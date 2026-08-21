package br.com.fiap.t2tdsps.projectmgt.mapper;

import br.com.fiap.t2tdsps.projectmgt.dto.ProjectDto;
import br.com.fiap.t2tdsps.projectmgt.model.Project;
import br.com.fiap.t2tdsps.projectmgt.model.ProjectStatus;

import java.util.List;

public final class ProjectMapper {

    private ProjectMapper() {
    }

    public static ProjectDto toDto(Project project) {
        return new ProjectDto(project.getId(), project.getName(),
                project.getDescription(),
                project.getStatus().toString(), project.getStartDate(),
                project.getEndDate());
    }

    public static Project toEntity(ProjectDto dto) {
        return new Project(dto.id(), dto.name(), dto.description(),
                ProjectStatus.valueOf(dto.status()),
                dto.startDate(), dto.endDate());
    }

    public static List<ProjectDto> toDto(List<Project> projects) {
        
        return projects.parallelStream()
                .map(
                        ProjectMapper::toDto
                )
                .toList();
    }
}
