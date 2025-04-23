package br.com.emersondias.ebd.repositories.specifications;

import br.com.emersondias.ebd.entities.Person;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class PersonSpecification {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    public static Specification<Person> birthdateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }

            String startFormatted = (startDate != null) ? startDate.format(DATE_FORMATTER) : null;
            String endFormatted = (endDate != null) ? endDate.format(DATE_FORMATTER) : null;

            List<Predicate> predicates = new ArrayList<>();

            if (nonNull(startFormatted) && nonNull(endFormatted)) {
                if (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                    predicates.add(criteriaBuilder.between(
                            criteriaBuilder.function("TO_CHAR", String.class, root.get("birthdate"), criteriaBuilder.literal("MM-DD")),
                            startFormatted,
                            endFormatted
                    ));
                } else {
                    Predicate afterStart = criteriaBuilder.greaterThanOrEqualTo(
                            criteriaBuilder.function("TO_CHAR", String.class, root.get("birthdate"), criteriaBuilder.literal("MM-DD")),
                            startFormatted
                    );
                    Predicate beforeEnd = criteriaBuilder.lessThanOrEqualTo(
                            criteriaBuilder.function("TO_CHAR", String.class, root.get("birthdate"), criteriaBuilder.literal("MM-DD")),
                            endFormatted
                    );
                    predicates.add(criteriaBuilder.or(afterStart, beforeEnd));
                }
            } else if (nonNull(startFormatted)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.function("TO_CHAR", String.class, root.get("birthdate"), criteriaBuilder.literal("MM-DD")),
                        startFormatted
                ));
            } else {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        criteriaBuilder.function("TO_CHAR", String.class, root.get("birthdate"), criteriaBuilder.literal("MM-DD")),
                        endFormatted
                ));
            }

            assert query != null;
            query.orderBy(criteriaBuilder.asc(
                    criteriaBuilder.function("TO_CHAR", String.class, root.get("birthdate"), criteriaBuilder.literal("MM-DD"))
            ));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
