package com.springAi.LernerManagementSystem.repository;

import com.springAi.LernerManagementSystem.entity.Learner;
import org.springframework.data.jpa.domain.Specification;

//JPA Criteria API (wrapped by Spring Specifications).
//1.What are the core components of the Criteria API?
//              The CriteriaBuilder to create conditions,
//              The CriteriaQuery to structure the query,
//              The Root to define the FROM clause and access entity attributes."
//***Conjuction*** The user didn't provide a city, we want to ignore this specific filter. By returning 1 = 1, when Hibernate chains this with other rules using AND, it looks like this: WHERE name = 'John' AND 1 = 1. The 1 = 1 does nothing, effectively ignoring the city check without breaking the SQL syntax.


public class LearnerSpecification {

    public static Specification<Learner> hasPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.isEmpty()) {
                return criteriaBuilder.conjunction(); // Ignore this filter if null
            }
            return criteriaBuilder.equal(root.get("learnerPhone"), phone);
        };
    }

    public static Specification<Learner> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // This means "ignore this condition"
            }
            // Equivalent to: WHERE learnerName LIKE '%name%'
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("learnerName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Learner> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            // Equivalent to: WHERE learnerEmail = 'email'
            return criteriaBuilder.equal(root.get("learnerEmail"), email);
        };
    }

}



