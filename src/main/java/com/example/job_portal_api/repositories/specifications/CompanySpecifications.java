package com.example.job_portal_api.repositories.specifications;

import com.example.job_portal_api.dtos.CompanySearchCriteria;
import com.example.job_portal_api.entities.Company;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class CompanySpecifications {

    public static Specification<Company> withCriteria(CompanySearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (criteria == null) {
                return predicate;
            }

            if (criteria.getName() != null) {
                String namePattern = "%" + criteria.getName().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("name")), namePattern
                ));
            }

            if (criteria.getDescription() != null) {
                String descriptionPattern = "%" + criteria.getDescription().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("description")), descriptionPattern
                ));
            }

            if (criteria.getEmail() != null) {
                String emailPattern = "%" + criteria.getEmail().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("email")), emailPattern
                ));
            }

            if (criteria.getWebsiteUrl() != null) {
                String websiteUrlPattern = "%" + criteria.getWebsiteUrl().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("websiteUrl")), websiteUrlPattern
                ));
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
