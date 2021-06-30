package com.uianz;


import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.test.context.TestConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestInstance(PER_CLASS)
@SpringBootTest(classes = Main.class)
public abstract class TestBase {

    @Autowired
    private List<ReactiveCrudRepository<?, ?>> repositories;

//    @AfterEach
//    public void clearRepositories() {
//        block(Flux.concat(repositories.stream()
//                                      .map(ReactiveCrudRepository::deleteAll)
//                                      .collect(Collectors.toList()))
//                  .collectList());
//    }

    @Nullable
    private <T> T block(Mono<T> mono) {
        return mono.block(Duration.ofSeconds(10));
    }

    @Nullable
    <T> List<T> block(Flux<T> flux) {
        return block(flux.collectList());
    }

    protected Mono<Void> given(Mono<?>... ts) {
        return Flux.concat(Stream.of(ts).collect(Collectors.toList())).last().then();
    }
}