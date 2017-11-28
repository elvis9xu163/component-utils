package com.xjd.utils.component.spring.data.delegate;

import org.springframework.data.repository.Repository;

/**
 * @author elvis.xu
 * @since 2017-11-27 11:43
 */
public interface DelegateRepository<T, ID> extends Delegate, Repository<T, ID> {
	@Override
	Repository<T, ID> getTarget();
}
