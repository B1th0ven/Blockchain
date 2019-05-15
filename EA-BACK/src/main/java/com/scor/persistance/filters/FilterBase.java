package com.scor.persistance.filters;

import javax.persistence.criteria.*;

import com.scor.persistance.entities.StudyEntity;

import java.io.Serializable;
import java.util.Optional;

public class FilterBase  implements Serializable {

    public static Predicate filter(Optional<String> query, String columnName, CriteriaBuilder cb, Root EntityRoot, Predicate p) {

        if (query.isPresent()) {
            String[] queryList = query.get().split(";");
            Predicate orp = cb.or();
            for (String q : queryList) {
                //System.out.println("FILTERING WITH -----> "+ columnName + " ----> " + query + " ---------->  "+ q);
                orp = cb.or(orp, cb.like(EntityRoot.get(columnName).as(String.class), "%" + q.trim() + "%"));
            }
            p = cb.and(p, orp);
        }
        return p;
    }

    public static Predicate filterJoin(Optional<String> query, String joinColumnName, String columnName, CriteriaBuilder cb, Root EntityRoot, Predicate p) {
        if (query.isPresent()) {
            String[] queryList = query.get().split(";");
            final Join<Object, Object> join = EntityRoot.join(joinColumnName, JoinType.LEFT);
            Predicate orp = cb.or();
            for (String q : queryList) {
                orp = cb.or(orp, cb.like(join.get(columnName), "%" + q.trim() + "%"));
            }
            p = cb.and(p, orp);
        }
        return p;
    }
}
