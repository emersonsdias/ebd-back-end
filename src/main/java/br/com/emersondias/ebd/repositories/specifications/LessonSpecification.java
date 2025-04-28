package br.com.emersondias.ebd.repositories.specifications;

import br.com.emersondias.ebd.dtos.filters.LessonFilterDTO;
import br.com.emersondias.ebd.entities.Lesson;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LessonSpecification {

    public static Specification<Lesson> withFilter(LessonFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }

            if (filter.getLessonNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("number"), filter.getLessonNumber()));
            }

            if (filter.getLessonStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getLessonStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Lesson> withFilterAndAuthenticatedPersonId(LessonFilterDTO filter, UUID personId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }

            if (filter.getLessonNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("number"), filter.getLessonNumber()));
            }

            if (filter.getLessonStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getLessonStatus()));
            }

            if (personId != null) {
                var classroomJoin = root.join("classroom");
                var teacherJoin = classroomJoin.join("teachers");
                var personJoin = teacherJoin.join("person");

                predicates.add(criteriaBuilder.equal(personJoin.get("id"), personId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
