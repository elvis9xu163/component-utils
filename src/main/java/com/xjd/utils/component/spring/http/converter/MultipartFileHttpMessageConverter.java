package com.xjd.utils.component.spring.http.converter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 支持MultipartFile的HttpMessageConverter
 * @author elvis.xu
 * @since 2017-05-09 11:17
 */
public class MultipartFileHttpMessageConverter implements HttpMessageConverter<MultipartFile> {
	protected List<MediaType> supportedMediaTypes = new ArrayList<MediaType>(0);
	protected ResourceHttpMessageConverter resourceHttpMessageConverter;

	public MultipartFileHttpMessageConverter() {
		resourceHttpMessageConverter = new ResourceHttpMessageConverter();
	}

	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		this.supportedMediaTypes = supportedMediaTypes;
	}

	public void setDefaultCharset(Charset charset) {
		this.resourceHttpMessageConverter.setDefaultCharset(charset);
	}

	public Charset getDefaultCharset() {
		return this.resourceHttpMessageConverter.getDefaultCharset();
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Collections.unmodifiableList(this.supportedMediaTypes);
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if (MultipartFile.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	public MultipartFile read(Class<? extends MultipartFile> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return null;
	}

	@Override
	public void write(MultipartFile file, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		MultipartFileResource multipartFileResource = new MultipartFileResource(file);
		resourceHttpMessageConverter.write(multipartFileResource, contentType, outputMessage);
	}

	public static class MultipartFileResource extends InputStreamResource {

		private final String filename;
		private final long size;

		public MultipartFileResource(MultipartFile multipartFile) throws IOException {
			this(multipartFile.getOriginalFilename(), multipartFile.getSize(), multipartFile.getInputStream());
		}

		public MultipartFileResource(String filename, long size, InputStream inputStream) {
			super(inputStream);
			this.size = size;
			this.filename = filename;
		}

		@Override
		public String getFilename() {
			return this.filename;
		}

		@Override
		public InputStream getInputStream() throws IOException, IllegalStateException {
			return super.getInputStream(); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public long contentLength() throws IOException {
			return size;
		}

	}
}
