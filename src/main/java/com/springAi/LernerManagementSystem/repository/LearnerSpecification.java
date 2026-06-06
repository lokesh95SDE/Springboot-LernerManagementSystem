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

    /**
     * Build a Specification that checks learnerPhone equals the provided phone.
     * <p>
     * Internals: Specifications are thin wrappers around the JPA Criteria API. The
     * returned lambda is not executed until the repository runs the query. If the phone
     * is null or empty we return {@code criteriaBuilder.conjunction()} which represents
     * a no-op predicate (1 = 1) so the filter is effectively ignored when combined with others.
     *
     * @param phone phone value to match
     * @return Specification for phone equality
     */
    public static Specification<Learner> hasPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.isEmpty()) {
                return criteriaBuilder.conjunction(); // Ignore this filter if null
            }
            return criteriaBuilder.equal(root.get("learnerPhone"), phone);
        };
    }

    /**
     * Build a Specification that performs a case-insensitive LIKE search on learnerName.
     *
     * Internals: The CriteriaBuilder constructs a LIKE predicate. We lower both sides to
     * achieve case-insensitive matching. If the name parameter is empty we return a
     * conjunction predicate so this condition is ignored by the final query.
     *
     * @param name partial name to match
     * @return Specification for name LIKE matching
     */
    public static Specification<Learner> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // This means "ignore this condition"
            }
            // Equivalent to: WHERE learnerName LIKE '%name%'
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("learnerName")), "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Build a Specification that checks learnerEmail equals the provided email.
     *
     * @param email email to match
     * @return Specification for email equality
     */
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
