package br.com.fiap.t2tdsps.projectmgt.service;

import br.com.fiap.t2tdsps.projectmgt.model.Project;
import br.com.fiap.t2tdsps.projectmgt.model.ProjectStatus;
import br.com.fiap.t2tdsps.projectmgt.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll(){
        return this.projectRepository.findAll();
    }

    //size = quantidade de itens por página
    // Exemplo pagina de 2 pagina, com paginas de ate 10 itens
    // OFFSET (pagina * quantiadade de itens por pagina)
    //vai fazer um query no db =>  select * from PROJECT OFFSET 10 LIMIT 10
    public Page<Project> findAll(Integer page, Integer size){
        return this.projectRepository.findAll(
                Pageable.ofSize(size).withPage(page)
        );
    }

    public Optional<Project> findById(Long id) {
        return this.projectRepository.findById(id);
    }

    public Project create(Project project) {
        project.setId(null); // garante que o cliente nunca escolhe o próprio id
        return this.projectRepository.save(project);
    }

    public Optional<Project> update(Long id, Project updated) {
        return this.projectRepository.findById(id).map(existing -> {
            updated.setId(id);
            return projectRepository.save(updated);
        });
    }

    public boolean delete(Long id) {
        if (!this.projectRepository.existsById(id)) {
            return false;
        }
        this.projectRepository.deleteById(id);
        return true;
    }

    public List<Project> findByStatus(ProjectStatus status) {
        return this.projectRepository.findByStatus(status);
    }

}
