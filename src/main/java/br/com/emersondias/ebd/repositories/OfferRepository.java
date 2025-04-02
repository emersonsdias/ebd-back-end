package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByLessonDateBetween(LocalDate startDate, LocalDate endDate);
}
