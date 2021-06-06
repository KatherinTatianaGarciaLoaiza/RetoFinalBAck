package com.sofka.retofinal.repository;

import com.sofka.retofinal.collections.KrEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface KrRepository extends ReactiveCrudRepository<KrEntity,String> {
    Flux<KrEntity> findAllByOkrId(String okrId);
}
