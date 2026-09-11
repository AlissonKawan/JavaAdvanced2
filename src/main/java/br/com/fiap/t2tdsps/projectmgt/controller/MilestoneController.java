package br.com.fiap.t2tdsps.projectmgt.controller;

import br.com.fiap.t2tdsps.projectmgt.assembler.MilestoneModelAssembler;
import br.com.fiap.t2tdsps.projectmgt.dto.MilestoneRequest;
import br.com.fiap.t2tdsps.projectmgt.dto.MilestoneResponse;
import br.com.fiap.t2tdsps.projectmgt.mapper.MilestoneMapper;
import br.com.fiap.t2tdsps.projectmgt.mapper.PageResponse;
import br.com.fiap.t2tdsps.projectmgt.model.Milestone;
import br.com.fiap.t2tdsps.projectmgt.model.MilestoneStatus;
import br.com.fiap.t2tdsps.projectmgt.service.MilestoneService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final MilestoneModelAssembler milestoneModelAssembler;

    public MilestoneController(MilestoneService milestoneService, MilestoneModelAssembler milestoneModelAssembler) {
        this.milestoneService = milestoneService;
        this.milestoneModelAssembler = milestoneModelAssembler;
    }

    // Decisão de escopo da Semana 3 (Exercício 2): combinar Page<T> com HATEOAS "de verdade" usa o
    // PagedResourcesAssembler do Spring HATEOAS, fora do escopo daqui - então o findAll perde o CollectionModel
    // e passa a devolver PageResponse<MilestoneResponse>. O findById continua com HATEOAS normalmente.
    @GetMapping
    public PageResponse<MilestoneResponse> findAll(@RequestParam(required = false) Long projectId,
                                                     @RequestParam(required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(required = false, defaultValue = "10") Integer sizePage) {
        Page<Milestone> pageResult = projectId != null
                ? milestoneService.findByProjectId(projectId, page, sizePage)
                : milestoneService.findAll(page, sizePage);

        return PageResponse.from(pageResult.map(MilestoneMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MilestoneResponse>> findById(@PathVariable Long id) {
        Milestone milestone = milestoneService.findById(id); // lança ResourceNotFoundException se não existir
        return ResponseEntity.ok(milestoneModelAssembler.toModel(milestone));
    }

    // Exemplo usando programacao funcional para enriquecer com mais links
    //      return milestoneService.findById(id)
    //            .map(m -> {
    //        EntityModel<MilestoneResponse> model = milestoneModelAssembler.toModel(m);
    //        model.add(linkTo(methodOn(MilestoneController.class).update(m.getId(), request)).withRel("update"));
    //        return model;
    //    })
    //            .map(ResponseEntity::ok)
    //                .orElseGet(() -> ResponseEntity.notFound().build());

    @PostMapping
    public ResponseEntity<MilestoneResponse> create(@RequestBody MilestoneRequest request) {
        Milestone milestone = new Milestone();
        milestone.setProjectId(request.projectId());
        milestone.setName(request.name());
        milestone.setDueDate(request.dueDate());
        milestone.setStatus(MilestoneStatus.PENDENTE); // toda milestone nasce PENDENTE

        Milestone created = milestoneService.create(milestone);
        return ResponseEntity.created(URI.create("/api/milestones/" + created.getId()))
                .body(MilestoneMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MilestoneResponse> update(@PathVariable Long id, @RequestBody MilestoneRequest request) {
        Milestone milestone = milestoneService.findById(id); // lança ResourceNotFoundException se não existir
        milestone.setProjectId(request.projectId());
        milestone.setName(request.name());
        milestone.setDueDate(request.dueDate());
        // status preservado, mesma lógica do Project: PUT aqui não mexe no ciclo de vida da milestone

        Milestone updated = milestoneService.update(id, milestone); // lança MilestoneAlreadyCompletedException se travada
        return ResponseEntity.ok(MilestoneMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        milestoneService.delete(id); // lança ResourceNotFoundException/MilestoneAlreadyCompletedException
        return ResponseEntity.noContent().build();
    }
}
