package com.sofka.retofinal.usecase;

import com.sofka.retofinal.mapper.MapperUtils;
import com.sofka.retofinal.model.OkrDTO;
import com.sofka.retofinal.repository.KrRepository;
import com.sofka.retofinal.repository.OkrRepository;
import com.sofka.retofinal.utils.Utilities;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class GetAllOkr implements Supplier<Flux<OkrDTO>> {
    private final OkrRepository okrRepository;
    private final MapperUtils mapperUtils;
    private final KrRepository krRepository;

    public GetAllOkr(OkrRepository okrRepository, MapperUtils mapperUtils, KrRepository krRepository) {
        this.okrRepository = okrRepository;
        this.mapperUtils = mapperUtils;
        this.krRepository = krRepository;
    }

    @Override
    public Flux<OkrDTO> get() {
        return okrRepository.findAll()
                .map(okrEntity -> mapperUtils.okrEntityToOkrDTO()
                .apply(okrEntity))
                .flatMap(joinOkrWithKr())
                .flatMap(Utilities::createProgressOkr);
    }

    public Function<OkrDTO, Mono<OkrDTO>> joinOkrWithKr() {
        return okrDto ->
                Mono.just(okrDto).zipWith(
                        krRepository.findAllByOkrId(okrDto.getId())
                                .map(mapperUtils.krEntityToKrDto())
                                .collectList(),
                        (okr, krs) -> {
                            okr.setKrs(krs);
                            return okr;
                        }
                );
    }
}
