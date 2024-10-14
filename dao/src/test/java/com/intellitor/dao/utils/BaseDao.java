package com.intellitor.dao.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BaseDao {

    default <T> T getReference(Class<T> cls, Serializable id) {
        return null;
    }

    default <T> Optional<T> getById(Class<T> cls, Serializable id) {
        return Optional.empty();
    }

    /**
     * Execute select using native query to ensure hibernate {@link org.hibernate.annotations.Where @Where} clause is not applied.
     * Common use case would be to include deleted record which are filtered out using @Where(clause = "deleted = 'false'")
     * @param cls
     *  result class
     * @param <T>
     *  entity type
     *
     */
    default <T> Optional<T> getByIdIgnoreWhereAnnotation(Class<T> cls, Serializable id) {
        return getById(cls, id);
    }

    default <T> List<T> listAll(Class<T> cls) {
        return new ArrayList<>();
    }

    /**
     * Execute select using native query to ensure hibernate {@link org.hibernate.annotations.Where @Where} clause is not applied.
     * Common use case would be to include deleted record which are filtered out using @Where(clause = "deleted = 'false'")
     * @param cls
     *  result class
     * @param <T>
     *  entity type
     *
     */
    default <T> List<T> listAllIgnoreWhereAnnotation(Class<T> cls) {
        return listAll(cls);
    }

    default <T> T save(T obj) {
        return null;
    }

    default void flush() {

    }

    default <T> void refresh(T obj) {

    }
}