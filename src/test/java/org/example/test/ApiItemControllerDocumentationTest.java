package org.example.test;

import org.example.test.controller.ApiItemController;
import org.example.test.domain.Item;
import org.example.test.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static reactor.core.publisher.Mono.when;

@WebFluxTest(controllers = ApiItemController.class)
@AutoConfigureRestDocs
public class ApiItemControllerDocumentationTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    ItemRepository repository;

    @Test
    void findingAllItems() {
        when(repository.findAll()).thenReturn(Flux.just(new Item("item-1", "Alf alarm clock", 19.99)));

        this.webTestClient.get().uri("/api/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findAll", preprocessResponse(prettyPrint())));
    }

    @Test
    void postNewItem() {
        when(repository.save(any())).thenReturn(Mono.just(new Item("item-1", "Alf alarm clock", 19.99)));

        this.webTestClient.post().uri("/api/items")
                .bodyValue(new Item("item-1", "Alf alarm clock", 19.99))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(document("post-new-item", preprocessResponse(prettyPrint())));
    }
}
