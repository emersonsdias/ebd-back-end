package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Person;
import br.com.emersondias.ebd.entities.enums.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID>, JpaSpecificationExecutor<Person> {
    List<Person> findByTypesInOrderByName(List<PersonType> types);

    @Query(value = "select p from Person p where p.active = false")
    List<Person> findInactive();

    @Query(value = """
            select p from Person p
            where p.id not in (select u.person.id from User u where u.person is not null)
            """)
    List<Person> findWithoutUser();

    @Query("""
            select p from Person p
            where (function('to_char', p.birthdate, 'MMDD') between function ('to_char', :startDate, 'MMDD')
            and function ('to_char', :endDate, 'MMDD'))
            order by function('to_char', p.birthdate, 'MMDD') asc
            """)
    List<Person> findByBirthdateCrossingYear(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
