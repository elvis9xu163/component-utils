package com.xjd.utils.biz.spring.core.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * Trim输入的参数
 */
public class StringTrimToNullConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		if (source == null) return null;
		source = source.trim();
		return source.length() == 0 ? null : source;
	}

}
