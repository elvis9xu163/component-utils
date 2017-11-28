package com.xjd.utils.component.spring.data.delegate;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author elvis.xu
 * @since 2017-11-27 14:16
 */
public interface DelegateMongoRepository<T, ID> extends DelegatePagingAndSortingRepository<T, ID>, DelegateQueryByExampleExecutor<T>, MongoRepository<T, ID> {

	@Override
	MongoRepository<T, ID> getTarget();

	@Override
	default <S extends T> List<S> saveAll(Iterable<S> entites) {
		return getTarget().saveAll(entites);
	}

	@Override
	default <S extends T> List<S> findAll(Example<S> example) {
		return getTarget().findAll(example);
	}

	@Override
	default <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return getTarget().findAll(example, sort);
	}

	@Override
	default List<T> findAll() {
		return getTarget().findAll();
	}

	@Override
	default List<T> findAll(Sort sort) {
		return getTarget().findAll(sort);
	}

	@Override
	default <S extends T> S insert(S entity) {
		return getTarget().insert(entity);
	}

	@Override
	default <S extends T> List<S> insert(Iterable<S> entities) {
		return getTarget().insert(entities);
	}
}
