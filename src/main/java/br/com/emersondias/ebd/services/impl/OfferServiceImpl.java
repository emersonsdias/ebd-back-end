package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.dtos.OfferDTO;
import br.com.emersondias.ebd.dtos.OfferDTOWithLesson;
import br.com.emersondias.ebd.mappers.OfferMapper;
import br.com.emersondias.ebd.repositories.OfferRepository;
import br.com.emersondias.ebd.services.interfaces.IOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements IOfferService {

    private final OfferRepository repository;

    @Override
    public List<OfferDTOWithLesson> findByPeriod(LocalDate startDate, LocalDate endDate) {
        requireNonNull(startDate);
        requireNonNull(endDate);
        return repository.findByLessonDateBetween(startDate, endDate).stream().map(OfferMapper::toDTOWithLesson).toList();
    }
}
