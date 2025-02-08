package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.AgeRange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeRangeRepository extends JpaRepository<AgeRange, Long> {
}
