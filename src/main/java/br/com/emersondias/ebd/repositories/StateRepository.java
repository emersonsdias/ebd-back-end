package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.location.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    List<State> findByActiveTrueOrderByName();
}
