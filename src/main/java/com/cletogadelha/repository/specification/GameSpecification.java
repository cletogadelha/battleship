package com.cletogadelha.repository.specification;

import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.cletogadelha.domain.Game;

public class GameSpecification {

    public static Specification<Game> byId(final UUID id) {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(Root<Game> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.<UUID>get("id"), id);
            }
        };
    }	

    public static Specification<Game> fetchData() {
        return new Specification<Game>() {
            @Override
            public Predicate toPredicate(Root<Game> root,
                    CriteriaQuery<?> query, CriteriaBuilder builder) {
                Class<?> clazz = query.getResultType();
                if (clazz.equals(Game.class)) {
                    root.fetch("playersOnGame");
                }
                return null;
            }
        };
    }
    

    public static Specification<Game> byIdWithCompleteFetch(UUID id) {
        return Specifications.where(byId(id)).and(fetchData());
    }

    
    
}