package com.scor.persistance.filters;


import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Optional;

public class SortBase  implements Serializable {

    public static void sort(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery q, Root entities, String sortColumnName,String defaultSortColumn) {
        Expression<Object> expression = cb.selectCase().when(cb.isNull(entities.get(sortColumnName)), 3).otherwise(2);
        if (desc.isPresent() && desc.get() == true) {
            q.orderBy(
                    cb.desc(expression),
                    cb.desc(entities.get(sortColumnName)),
                    cb.desc(entities.get(defaultSortColumn))
            );
        } else {
            q.orderBy(
                    cb.asc(expression),
                    cb.asc(entities.get(sortColumnName)),
                    cb.asc(entities.get(defaultSortColumn))
            );
        }

    }

    public static void sort(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery q, Root entities, String sortColumnName) {
        Expression<Object> expression = cb.selectCase().when(cb.isNull(entities.get(sortColumnName)), 3).otherwise(2);
        if (desc.isPresent() && desc.get() == true) {
            q.orderBy(
                    cb.desc(expression),
                    cb.desc(entities.get(sortColumnName))
                    //cb.desc(studyEntities.get("stId"))
                    );
        } else {
            q.orderBy(
                    cb.asc(expression),
                    cb.asc(entities.get(sortColumnName))
                    //cb.asc(studyEntities.get("stId"))
                    );
        }

    }

    public static void sortJoin(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery q, Root entities, String joinColumnName, String sortColumnName,String defaultSortColumn) {
        Join<Object, Object> join = entities.join(joinColumnName, JoinType.LEFT);
        if (desc.isPresent() && desc.get() == true) {
            q.orderBy(cb.desc(join.get(sortColumnName)),
                    cb.desc(entities.get(defaultSortColumn))
            );
        } else {
            q.orderBy(cb.asc(join.get(sortColumnName)),

                    cb.asc(entities.get(defaultSortColumn))
            );
        }
    }

    public static void sortJoin(Optional<Boolean> desc, CriteriaBuilder cb, CriteriaQuery q, Root entities, String joinColumnName, String sortColumnName) {
        Join<Object, Object> join = entities.join(joinColumnName, JoinType.LEFT);
        if (desc.isPresent() && desc.get() == true) {
            q.orderBy(cb.desc(join.get(sortColumnName))
                    //, cb.desc(entities.get("stId"))
            );
        } else {
            q.orderBy(cb.asc(join.get(sortColumnName))
                    // , cb.asc(entities.get("stId"))
            );
        }
    }
}
