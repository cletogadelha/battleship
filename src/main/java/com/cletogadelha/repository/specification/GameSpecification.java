package com.cletogadelha.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.cletogadelha.domain.Game;

public class GameSpecification {

    public static Specification<Game> byId(final Integer id) {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(Root<Game> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.<Integer>get("id"), id);
            }
        };
    }

    public static Specification<Game> fetchSimpleData() {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(Root<Game> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                Class<?> clazz = query.getResultType();
                if (clazz.equals(Game.class)) {
                    root.fetch("playersOnGame", JoinType.LEFT);
                }
                return null;
            }
        };
    }
    
    public static Specification<Game> fetchCompleteData() {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(Root<Game> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                Class<?> clazz = query.getResultType();
                if (clazz.equals(Game.class)) {
                    root.fetch("playersOnGame", JoinType.LEFT);
                    root.fetch("moves", JoinType.LEFT);
                }
                return null;
            }
        };
    }
    
    public static Specification<Game> byIdWithSimpleFetch(Integer id) {
        return Specifications.where(byId(id)).and(fetchSimpleData());
    }
    
    public static Specification<Game> byIdWithCompleteFetch(Integer id) {
        return Specifications.where(byId(id)).and(fetchCompleteData());
    }
    
}