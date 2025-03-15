package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.location.CitySimpleDTO;
import br.com.emersondias.ebd.dtos.location.StateDTO;
import br.com.emersondias.ebd.entities.location.City;
import br.com.emersondias.ebd.entities.location.State;
import br.com.emersondias.ebd.mappers.CityMapper;
import br.com.emersondias.ebd.mappers.StateMapper;
import br.com.emersondias.ebd.repositories.CityRepository;
import br.com.emersondias.ebd.repositories.StateRepository;
import br.com.emersondias.ebd.services.interfaces.ILocationService;
import br.com.emersondias.ebd.utils.LogHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements ILocationService {

    private static final LogHelper LOG = LogHelper.getInstance();

    private static final String PATH_IBGE = "https://servicodados.ibge.gov.br/api/v1/localidades";
    private final RestTemplate restTemplate;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    @Transactional
    @Override
    public void syncCitiesFromIBGE() {
        var start = Instant.now();
        LOG.info("Start sync cities from IBGE");
        record IBGEState(Long id) {
        }
        record IBGEMesoregion(IBGEState UF) {
        }
        record IBGEMicrorregion(IBGEMesoregion mesorregiao) {
        }
        record IBGECity(Long id, String nome, IBGEMicrorregion microrregiao) {
        }
        var cities = Stream.of(requireNonNull(restTemplate.getForObject(PATH_IBGE + "/municipios", IBGECity[].class)))
                .map(ibgeCity -> City.builder()
                        .id(ibgeCity.id)
                        .name(ibgeCity.nome)
                        .state(State.builder().id(ibgeCity.microrregiao.mesorregiao.UF.id).build())
                        .active(true)
                        .build()
                ).toList();
        cityRepository.saveAll(cities);
        LOG.info("End sync cities from IBGE. Elapsed time: [" + (Instant.now().toEpochMilli() - start.toEpochMilli()) + "ms]");
    }

    @Transactional
    @Override
    public void syncStatesFromIBGE() {
        var start = Instant.now();
        LOG.info("Start sync states from IBGE");
        record IBGEState(Long id, String sigla, String nome) {
        }
        var states = Stream.of(requireNonNull(restTemplate.getForObject(PATH_IBGE + "/estados", IBGEState[].class)))
                .map(ibgeState -> State.builder().id(ibgeState.id)
                        .abbreviation(ibgeState.sigla)
                        .name(ibgeState.nome)
                        .active(true)
                        .build()
                ).toList();
        stateRepository.saveAll(states);
        LOG.info("End sync states from IBGE. Elapsed time: [" + (Instant.now().toEpochMilli() - start.toEpochMilli()) + "ms]");
    }

    @Override
    public List<StateDTO> findAllStates() {
        return stateRepository.findByActiveTrueOrderByName().stream().map(StateMapper::toDTO).toList();
    }

    @Override
    public List<CitySimpleDTO> findCitiesByStateId(Long stateId) {
        return cityRepository.findByStateIdAndActiveTrue(stateId).stream().map(CityMapper::toSimpleDTO).toList();
    }

}
