package com.xjd.utils.component.spring.data.delegate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author elvis.xu
 * @since 2017-11-27 14:12
 */
public interface DelegatePagingAndSortingRepository<T, ID> extends DelegateCrudRepository<T, ID>, PagingAndSortingRepository<T, ID> {
	@Override
	PagingAndSortingRepository<T, ID> getTarget();

	@Override
	default Iterable<T> findAll(Sort sort) {
		return getTarget().findAll(sort);
	}

	@Override
	default Page<T> findAll(Pageable pageable) {
		return getTarget().findAll(pageable);
	}
}
