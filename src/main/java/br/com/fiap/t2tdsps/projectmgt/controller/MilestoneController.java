package br.com.fiap.t2tdsps.projectmgt.controller;

import br.com.fiap.t2tdsps.projectmgt.assembler.MilestoneModelAssembler;
import br.com.fiap.t2tdsps.projectmgt.model.Milestone;
import br.com.fiap.t2tdsps.projectmgt.service.MilestoneService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final MilestoneModelAssembler milestoneModelAssembler;

    public MilestoneController(MilestoneService milestoneService, MilestoneModelAssembler milestoneModelAssembler) {
        this.milestoneService = milestoneService;
        this.milestoneModelAssembler = milestoneModelAssembler;
    }

    @GetMapping
    public List<Milestone> findAll() {
        return milestoneService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Milestone>> findById(@PathVariable Long id) {
        return milestoneService.findById(id)
                .map(milestoneModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

// Exemplo usando programacao funcional para enriquecer com mais links
//      return milestoneService.findById(id)
//            .map(m -> {
//        EntityModel<Milestone> model = milestoneModelAssembler.toModel(m);
//        model.add(linkTo(methodOn(MilestoneController.class).update(m.getId(),m)).withRel("update"));
//        return model;
//    })
//            .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());




    @PostMapping
    public ResponseEntity<Milestone> create(@RequestBody Milestone milestone) {
        Milestone created = milestoneService.create(milestone);
        return ResponseEntity.created(URI.create("/api/milestones/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Milestone> update(@PathVariable Long id, @RequestBody Milestone milestone) {
        return milestoneService.update(id, milestone)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (milestoneService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
