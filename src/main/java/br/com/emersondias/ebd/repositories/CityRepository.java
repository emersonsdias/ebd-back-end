package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.location.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStateIdAndActiveTrue(Long stateId);
}
