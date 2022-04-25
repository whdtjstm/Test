package org.example.test.controller;

import org.example.test.domain.Item;
import org.example.test.repository.ItemRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
public class HypermediaItemController {
    private final ItemRepository repository;

    public HypermediaItemController(ItemRepository repository) {
        this.repository = repository;
    }

    Flux<Item> findAll() {
        return this.repository.findAll();
    }

    Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(id))
                .withSelfRel()
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll())
                .withRel(IanaLinkRelations.ITEM)
                .toMono();

        return Mono.zip(repository.findById(id), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }
}
