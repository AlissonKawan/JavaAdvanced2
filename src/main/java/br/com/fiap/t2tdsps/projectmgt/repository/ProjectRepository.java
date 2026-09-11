package br.com.fiap.t2tdsps.projectmgt.repository;

import br.com.fiap.t2tdsps.projectmgt.model.Project;
import br.com.fiap.t2tdsps.projectmgt.model.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(ProjectStatus status); // derived query method - sem implementação escrita

    // mesmo derived query method, agora aceitando Pageable - Spring Data resolve os dois sozinho
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);
}
