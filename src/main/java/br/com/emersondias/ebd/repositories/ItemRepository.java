package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByActiveTrue();

    Optional<Item> findByName(String name);
}
