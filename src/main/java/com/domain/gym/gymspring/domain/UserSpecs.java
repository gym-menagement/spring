package com.domain.gym.gymspring.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecs {
    public enum SearchKey {
        LOGINID("loginid"),
        NAME("name"),
        GYM("gym");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<User> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<User>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<User> root,
            CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case LOGINID:
                case NAME:
                    predicate.add(builder.like(root.get(key.value), "%" + searchKeyword.get(key) + "%"));
                case GYM:
                    predicate.add(builder.equal(root.get(key.value), searchKeyword.get(key)));
                    break;
            }
        }
        return predicate;
    }
}
