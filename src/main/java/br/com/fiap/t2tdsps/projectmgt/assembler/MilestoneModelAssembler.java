package br.com.fiap.t2tdsps.projectmgt.assembler;

import br.com.fiap.t2tdsps.projectmgt.controller.MilestoneController;
import br.com.fiap.t2tdsps.projectmgt.model.Milestone;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MilestoneModelAssembler implements RepresentationModelAssembler<Milestone, EntityModel<Milestone>> {

    @Override
    public EntityModel<Milestone> toModel(Milestone milestone) {

        EntityModel<Milestone> model = EntityModel.of(milestone);
        model.add(linkTo(methodOn(MilestoneController.class).findById(milestone.getId())).withSelfRel());
        model.add(linkTo(methodOn(MilestoneController.class).findAll()).withRel("findAll"));
        model.add(linkTo(methodOn(MilestoneController.class).delete(milestone.getId())).withRel("delete"));

        return model;
    }
}
