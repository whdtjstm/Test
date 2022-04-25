package org.example.test.controller;

import org.example.test.domain.Item;
import org.example.test.repository.ItemRepository;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
public class AffordancesItemController {
    private final ItemRepository repository;

    public AffordancesItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/affordances/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<EntityModel<Item>> item, @PathVariable String id) {
        return item.map(EntityModel::getContent)
                .map(content -> new Item(id, content.getName(), content.getPrice()))
                .flatMap(this.repository::save)
                .then(findOne(id))
                .map(model -> ResponseEntity.noContent().location(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).build());
    }

    @PostMapping("/affordances/items")
    Mono<ResponseEntity<Item>> addNewItem(@RequestBody Mono<EntityModel<Item>> item) {
        return item.map(EntityModel::getContent)
                .flatMap(this.repository::save)
                .map(Item::getId)
                .flatMap(this::findOne)
                .map(newModel -> ResponseEntity.created(newModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(newModel.getContent()));
    }

    @GetMapping("/affordances/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll() {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> aggregateLink = linkTo(controller.findAll())
                .withSelfRel()
                .andAffordance(controller.addNewItem(null))
                .toMono();

        return this.repository.findAll()
                .flatMap(item -> findOne(item.getId()))
                .collectList()
                .flatMap(models -> aggregateLink.map(selfLink -> CollectionModel.of(models, selfLink)));
    }

    @GetMapping("/affordances/items/{id}")
    Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(id))
                .withSelfRel()
                .andAffordance(controller.updateItem(null, id))
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll())
                .withRel(IanaLinkRelations.ITEM)
                .toMono();

        return Mono.zip(repository.findById(id), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }
}
