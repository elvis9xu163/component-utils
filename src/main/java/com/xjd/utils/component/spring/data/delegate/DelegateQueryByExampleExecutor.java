package com.xjd.utils.component.spring.data.delegate;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author elvis.xu
 * @since 2017-11-27 14:18
 */
public interface DelegateQueryByExampleExecutor<T> extends Delegate, QueryByExampleExecutor<T> {

	@Override
	QueryByExampleExecutor<T> getTarget();

	@Override
	default <S extends T> Optional<S> findOne(Example<S> example) {
		return getTarget().findOne(example);
	}

	@Override
	default <S extends T> Iterable<S> findAll(Example<S> example) {
		return getTarget().findAll(example);
	}

	@Override
	default <S extends T> Iterable<S> findAll(Example<S> example, Sort sort) {
		return getTarget().findAll(example, sort);
	}

	@Override
	default <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		return getTarget().findAll(example, pageable);
	}

	@Override
	default <S extends T> long count(Example<S> example) {
		return getTarget().count(example);
	}

	@Override
	default <S extends T> boolean exists(Example<S> example) {
		return getTarget().exists(example);
	}
}
