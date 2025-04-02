package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.dtos.OfferDTOWithLesson;

import java.time.LocalDate;
import java.util.List;

public interface IOfferService {

    List<OfferDTOWithLesson> findByPeriod(LocalDate startDate, LocalDate endDate);
}
