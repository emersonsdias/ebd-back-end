package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.enums.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    List<Person> findByTypesInOrderByName(List<PersonType> types);

    @Query(value = "select p from Person p where p.active = false")
    List<Person> findInactive();
}
