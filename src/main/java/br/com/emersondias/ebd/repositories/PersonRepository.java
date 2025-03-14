package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.enums.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    List<Person> findByTypesInOrderByName(List<PersonType> types);
}
