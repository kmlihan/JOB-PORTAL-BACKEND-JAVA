package com.example.job_portal_api.repositories.specifications;


import com.example.job_portal_api.dtos.UserSearchCriteria;
import com.example.job_portal_api.entities.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class UserSpecifications {
    public static Specification<User> withCriteria(UserSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (criteria == null) {
                return predicate; // Return an always true predicate if criteria is null
            }

            if (criteria.getFirstName() != null) {
                String firstNamePattern = "%" + criteria.getFirstName().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("firstName")), firstNamePattern
                ));
            }

            if (criteria.getLastName() != null) {
                String lastNamePattern = "%" + criteria.getLastName().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("lastName")), lastNamePattern
                ));
            }

            if (criteria.getEmail() != null) {
                String emailPattern = "%" + criteria.getEmail().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("email")), emailPattern
                ));
            }

            if (criteria.getPhone() != null) {
                String phonePattern = "%" + criteria.getPhone().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("phone")), phonePattern
                ));
            }

            if (criteria.getBirthDate() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("birthDate"), criteria.getBirthDate()));
            }

            if (criteria.getNationality() != null) {
                String nationalityPattern = "%" + criteria.getNationality().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("nationality")), nationalityPattern
                ));
            }

            if (criteria.getProfileTitle() != null) {
                String profileTitlePattern = "%" + criteria.getProfileTitle().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("profileTitle")), profileTitlePattern
                ));
            }

            if (criteria.getSkills() != null && !criteria.getSkills().isEmpty()) {
                Predicate[] skillPredicates = criteria.getSkills().stream()
                        .map(skill -> {
                            String skillPattern = "%" + skill.toUpperCase(Locale.ROOT) + "%";
                            return criteriaBuilder.like(
                                    criteriaBuilder.upper(root.join("skills").as(String.class)), skillPattern
                            );
                        })
                        .toArray(Predicate[]::new);

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(skillPredicates));
            }

            if (criteria.getLanguages() != null && !criteria.getLanguages().isEmpty()) {
                Predicate[] languagePredicates = criteria.getLanguages().stream()
                        .map(language -> {
                            String languagePattern = "%" + language.toUpperCase(Locale.ROOT) + "%";
                            return criteriaBuilder.like(
                                    criteriaBuilder.upper(root.join("languages").as(String.class)), languagePattern
                            );
                        })
                        .toArray(Predicate[]::new);

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(languagePredicates));
            }

            if (criteria.getArchived() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("archived"), criteria.getArchived()));
            }

            if (criteria.getCity() != null) {
                String cityPattern = "%" + criteria.getCity().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("address").get("city")), cityPattern
                ));
            }

            if (criteria.getCountry() != null) {
                String countryPattern = "%" + criteria.getCountry().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("address").get("country").get("name")), countryPattern
                ));
            }

            return predicate;
        };
    }
}
