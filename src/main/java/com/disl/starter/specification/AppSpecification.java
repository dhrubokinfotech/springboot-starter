package com.disl.starter.specification;


import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppSpecification {

    public static <T> Specification<T> getSpecification(Map<String, Object> parameters) {
        return Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String filterBy = entry.getKey();
                String filterWith = entry.getValue().toString();

                if(filterWith != null && !filterWith.isEmpty()) {
                    Class<?> type = root.get(filterBy).getJavaType();

                    if (type.equals(Long.class)) {
                        predicates.add(criteriaBuilder.equal(root.get(filterBy), Long.valueOf(filterWith)));
                    } else if (type.equals(Boolean.class)) {
                        predicates.add(criteriaBuilder.equal(root.get(filterBy), Boolean.valueOf(filterWith)));
                    } else if (type.equals(String.class)) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(filterBy)), "%" + filterWith.toUpperCase() + "%"));
                    } else if (type.equals(LocalDateTime.class)) {
                        LocalDate localDate = LocalDate.parse(filterWith);
                        LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
                        LocalDateTime endDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
                        predicates.add(criteriaBuilder.between(root.get(filterBy), startDateTime, endDateTime));
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get(filterBy), filterWith));
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
    }
}
