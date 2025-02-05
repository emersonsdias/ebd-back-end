package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.location.CitySimpleDTO;
import br.com.emersondias.ebd.dtos.location.StateDTO;

import java.util.List;

public interface ILocationService {

    void syncCitiesFromIBGE();

    void syncStatesFromIBGE();

    List<StateDTO> findAllStates();

    List<CitySimpleDTO> findCitiesByStateId(Long stateId);
}
