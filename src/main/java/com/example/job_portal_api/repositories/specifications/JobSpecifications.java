package com.example.job_portal_api.repositories.specifications;

import com.example.job_portal_api.dtos.JobSearchCriteria;
import com.example.job_portal_api.entities.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class JobSpecifications {
    public static Specification<Job> withCriteria(JobSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (criteria == null) {
                return predicate;
            }

            if (criteria.getTitle() != null)  {
                String titlePattern = "%" + criteria.getTitle().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("title")), titlePattern
                ));
            }

            if (criteria.getDescription() != null) {
                String descriptionPattern = "%" + criteria.getDescription().toUpperCase(Locale.ROOT) + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("description")), descriptionPattern
                ));
            }


            if (criteria.getExperience() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("experience"), criteria.getExperience()));
            }

            if (criteria.getSalary() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), criteria.getSalary()));
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


            if (criteria.getContractType() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("contractType"), criteria.getContractType()));
            }

            if (criteria.getCompanyId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("company").get("id"), criteria.getCompanyId()));
            }

            if (criteria.getJobType() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("jobType"), criteria.getJobType()));
            }

            if (criteria.getLevelOfEducation() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("levelOfEducation"), criteria.getLevelOfEducation()));
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