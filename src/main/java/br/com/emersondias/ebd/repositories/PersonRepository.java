package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
}
