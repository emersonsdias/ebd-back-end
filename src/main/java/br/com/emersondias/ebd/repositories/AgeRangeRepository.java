package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.AgeRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgeRangeRepository extends JpaRepository<AgeRange, Long> {
    Optional<AgeRange> findByName(String name);
}
