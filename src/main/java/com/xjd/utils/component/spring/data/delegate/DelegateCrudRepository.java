package com.xjd.utils.component.spring.data.delegate;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * @author elvis.xu
 * @since 2017-11-27 11:46
 */
public interface DelegateCrudRepository<T, ID> extends DelegateRepository<T, ID>, CrudRepository<T, ID> {

	@Override
	CrudRepository<T, ID> getTarget();

	@Override
	default  <S extends T> S save(S entity) {
		return getTarget().save(entity);
	}

	@Override
	default  <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
		return getTarget().saveAll(entities);
	}

	@Override
	default Optional<T> findById(ID id) {
		return getTarget().findById(id);
	}

	@Override
	default boolean existsById(ID id) {
		return getTarget().existsById(id);
	}

	@Override
	default Iterable<T> findAll() {
		return getTarget().findAll();
	}

	@Override
	default Iterable<T> findAllById(Iterable<ID> ids) {
		return getTarget().findAllById(ids);
	}

	@Override
	default long count() {
		return getTarget().count();
	}

	@Override
	default void deleteById(ID id) {
		getTarget().deleteById(id);
	}

	@Override
	default void delete(T entity) {
		getTarget().delete(entity);
	}

	@Override
	default void deleteAll(Iterable<? extends T> entities) {
		getTarget().deleteAll(entities);
	}

	@Override
	default void deleteAll() {
		getTarget().deleteAll();
	}
}
