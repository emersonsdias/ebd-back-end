package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
