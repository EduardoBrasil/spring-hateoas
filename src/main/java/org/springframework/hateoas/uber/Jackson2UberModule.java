/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.hateoas.uber;

import static org.springframework.hateoas.JacksonHelper.*;
import static org.springframework.hateoas.PagedResources.*;
import static org.springframework.hateoas.uber.UberData.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.support.JacksonHelper;
import org.springframework.hateoas.support.PropertyUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * @author Greg Turnquist
 */
public class Jackson2UberModule extends SimpleModule {

	public Jackson2UberModule() {
		super("uber-module", new Version(1, 0, 0, null, "org.springframework.hateoas", "spring-hateoas"));
		
		setMixInAnnotation(ResourceSupport.class, ResourceSupportMixin.class);
		setMixInAnnotation(Resource.class, ResourceMixin.class);
		setMixInAnnotation(Resources.class, ResourcesMixin.class);
		setMixInAnnotation(PagedResources.class, PagedResourcesMixin.class);
	}

	static class UberResourceSupportSerializer extends ContainerSerializer<ResourceSupport> implements ContextualSerializer {

		private final BeanProperty property;

		UberResourceSupportSerializer(BeanProperty property) {

			super(ResourceSupport.class, false);
			this.property = property;
		}

		UberResourceSupportSerializer() {
			this(null);
		}

		@Override
		public void serialize(ResourceSupport value, JsonGenerator gen, SerializerProvider provider) throws IOException {

			UberDocument doc = new UberDocument()
				.withUber(new Uber()
					.withVersion("1.0")
					.withData(toUberData(value).getData()));

			provider
				.findValueSerializer(UberDocument.class, property)
				.serialize(doc, gen, provider);
		}

		@Override
		public JavaType getContentType() {
			return null;
		}

		@Override
		public JsonSerializer<?> getContentSerializer() {
			return null;
		}

		@Override
		public boolean hasSingleElement(ResourceSupport value) {
			return false;
		}

		@Override
		protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
			return null;
		}

		@Override
		public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
			return new UberResourceSupportSerializer(property);
		}
	}

	static class UberResourceSerializer extends ContainerSerializer<Resource<?>> implements ContextualSerializer {

		private final BeanProperty property;

		UberResourceSerializer(BeanProperty property) {

			super(Resource.class, false);
			this.property = property;
		}

		UberResourceSerializer() {
			this(null);
		}

		@Override
		public void serialize(Resource<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {

			UberDocument doc = new UberDocument()
				.withUber(new Uber()
					.withVersion("1.0")
					.withData(toUberData(value).getData()));

			provider
				.findValueSerializer(UberDocument.class, property)
				.serialize(doc, gen, provider);
		}

		@Override
		public JavaType getContentType() {
			return null;
		}

		@Override
		public JsonSerializer<?> getContentSerializer() {
			return null;
		}

		@Override
		public boolean hasSingleElement(Resource<?> value) {
			return false;
		}

		@Override
		protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
			return null;
		}

		@Override
		public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
			return new UberResourceSerializer(property);
		}
	}

	static class UberResourcesSerializer extends ContainerSerializer<Resources<?>> implements ContextualSerializer {

		private BeanProperty property;

		UberResourcesSerializer(BeanProperty property) {

			super(Resources.class, false);
			this.property = property;
		}

		UberResourcesSerializer() {
			this(null);
		}
		
		@Override
		public void serialize(Resources<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {

			UberDocument doc = new UberDocument()
				.withUber(new Uber()
					.withVersion("1.0")
					.withData(toUberData(value)));

			provider
				.findValueSerializer(UberDocument.class, property)
				.serialize(doc, gen, provider);
		}

		@Override
		public JavaType getContentType() {
			return null;
		}

		@Override
		public JsonSerializer<?> getContentSerializer() {
			return null;
		}

		@Override
		public boolean hasSingleElement(Resources<?> value) {
			return false;
		}

		@Override
		protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
			return null;
		}

		@Override
		public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
			return new UberResourcesSerializer(property);
		}
	}

	static class UberPagedResourcesSerializer extends ContainerSerializer<PagedResources<?>> implements ContextualSerializer {

		private BeanProperty property;

		UberPagedResourcesSerializer(BeanProperty property) {

			super(PagedResources.class, false);
			this.property = property;
		}

		UberPagedResourcesSerializer() {
			this(null);
		}

		@Override
		public void serialize(PagedResources<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {

			UberDocument doc = new UberDocument()
				.withUber(new Uber()
					.withVersion("1.0")
					.withData(toUberData(value)));

			provider
				.findValueSerializer(UberDocument.class, property)
				.serialize(doc, gen, provider);
		}

		@Override
		public JavaType getContentType() {
			return null;
		}

		@Override
		public JsonSerializer<?> getContentSerializer() {
			return null;
		}

		@Override
		public boolean hasSingleElement(PagedResources<?> value) {
			return value.getContent().size() == 1;
		}

		@Override
		protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
			return null;
		}

		@Override
		public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
			return new UberPagedResourcesSerializer(property);
		}
	}

	static class UberActionSerializer extends StdSerializer<UberAction> {

		UberActionSerializer() {
			super(UberAction.class);
		}

		@Override
		public void serialize(UberAction value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeString(value.toString());
		}
	}

	static class UberResourceSupportDeserializer extends ContainerDeserializerBase<ResourceSupport> implements ContextualDeserializer {

		private JavaType contentType;

		UberResourceSupportDeserializer(JavaType contentType) {

			super(contentType);
			this.contentType = contentType;
		}

		UberResourceSupportDeserializer() {
			this(TypeFactory.defaultInstance().constructSimpleType(UberDocument.class, new JavaType[0]));
		}
		

		@Override
		public ResourceSupport deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

			UberDocument doc = p.getCodec().readValue(p, UberDocument.class);

			return doc.getUber().getData().stream()
				.filter(uberData -> !StringUtils.isEmpty(uberData.getName()))
				.findFirst()
				.map(uberData -> {
					Map<String, Object> properties = uberData.getData().stream()
						.collect(Collectors.toMap(UberData::getName, UberData::getValue));
					ResourceSupport obj = (ResourceSupport) PropertyUtils.createObjectFromProperties(this.getContentType().getRawClass(), properties);
					obj.add(doc.getUber().getLinks());
					return obj;
				})
				.orElseGet(() -> {
					ResourceSupport resourceSupport = new ResourceSupport();
					resourceSupport.add(doc.getUber().getLinks());
					return resourceSupport;
				});
		}

		@Override
		public JavaType getContentType() {
			return this.contentType;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {

			if (property != null) {
				JavaType vc = property.getType().getContentType();
				return new UberResourceSupportDeserializer(vc);
			} else {
				return new UberResourceSupportDeserializer(ctxt.getContextualType());
			}
		}

		/**
		 * Accesor for deserializer use for deserializing content values.
		 */
		@Override
		public JsonDeserializer<Object> getContentDeserializer() {
			return null;
		}
	}

	static class UberResourceDeserializer extends ContainerDeserializerBase<Resource<?>> implements ContextualDeserializer {

		private JavaType contentType;

		UberResourceDeserializer(JavaType contentType) {

			super(contentType);
			this.contentType = contentType;
		}

		UberResourceDeserializer() {
			this(TypeFactory.defaultInstance().constructSimpleType(UberDocument.class, new JavaType[0]));
		}

		@Override
		public Resource<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

			UberDocument doc = p.getCodec().readValue(p, UberDocument.class);

			return doc.getUber().getData().stream()
				.filter(uberData -> !StringUtils.isEmpty(uberData.getName()))
				.findFirst()
				.map(uberData -> {

					if (uberData.getData().size() == 1 && uberData.getData().get(0).getName() == null) {
						// Primitive type
						return new Resource<>(uberData.getData().get(0).getValue(), doc.getUber().getLinks());
					}

					Map<String, Object> properties = uberData.getData().stream()
						.collect(Collectors.toMap(UberData::getName, UberData::getValue));
					JavaType rootType = JacksonHelper.findRootType(this.contentType);
					Object value = PropertyUtils.createObjectFromProperties(rootType.getRawClass(), properties);
					return new Resource<>(value, doc.getUber().getLinks());
				})
				.orElseThrow(() -> new IllegalStateException("No data entry containing a 'value' was found in this document!"));
		}

		@Override
		public JavaType getContentType() {
			return this.contentType;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {

			if (property != null) {
				JavaType vc = property.getType().getContentType();
				return new UberResourceDeserializer(vc);
			} else {
				return new UberResourceDeserializer(ctxt.getContextualType());
			}
		}

		/**
		 * Accesor for deserializer use for deserializing content values.
		 */
		@Override
		public JsonDeserializer<Object> getContentDeserializer() {
			return null;
		}
	}

	static class UberResourcesDeserializer extends ContainerDeserializerBase<Resources<?>> implements ContextualDeserializer {

		private JavaType contentType;

		UberResourcesDeserializer(JavaType contentType) {

			super(contentType);
			this.contentType = contentType;
		}

		UberResourcesDeserializer() {
			this(TypeFactory.defaultInstance().constructSimpleType(UberDocument.class, new JavaType[0]));
		}

		@Override
		public Resources<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

			JavaType rootType = JacksonHelper.findRootType(this.contentType);

			UberDocument doc = p.getCodec().readValue(p, UberDocument.class);

			return uberDocumentToResources(doc, rootType, this.contentType);
		}

		/**
		 * Accessor for declared type of contained value elements; either exact
		 * type, or one of its supertypes.
		 */
		@Override
		public JavaType getContentType() {
			return this.contentType;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {

			if (property != null) {
				JavaType vc = property.getType().getContentType();
				return new UberResourcesDeserializer(vc);
			} else {
				return new UberResourcesDeserializer(ctxt.getContextualType());
			}
		}

		/**
		 * Accesor for deserializer use for deserializing content values.
		 */
		@Override
		public JsonDeserializer<Object> getContentDeserializer() {
			return null;
		}
	}

	private static Resources<?> uberDocumentToResources(UberDocument doc, JavaType rootType, JavaType contentType) {

		List<Object> content = new ArrayList<>();

		for (UberData uberData : doc.getUber().getData()) {

			if (uberData.getName() != null && uberData.getName().equals("page")) {
				continue;
			}

			if (uberData.getLinks().isEmpty()) {

				List<Link> resourceLinks = new ArrayList<>();
				Resource<?> resource = null;

				for (UberData item : uberData.getData()) {
					if (item.getRel() != null) {
						item.getRel().forEach(rel -> resourceLinks.add(new Link(item.getUrl(), rel)));
					} else {

						if (item.getData().size() == 1 && item.getData().get(0).getName() == null) {
							// Primitive type
							resource = new Resource<>(item.getData().get(0).getValue(), uberData.getLinks());
						} else {

							Map<String, Object> properties = item.getData().stream()
								.collect(Collectors.toMap(UberData::getName, UberData::getValue));
							Object obj = PropertyUtils.createObjectFromProperties(rootType.getRawClass(), properties);
							resource = new Resource<>(obj, uberData.getLinks());
						}
					}
				}

				if (resource != null) {
					resource.add(resourceLinks);
					content.add(resource);
				} else {
					throw new RuntimeException("No content!");
				}
			}
		}

		if (isResourcesOfResource(contentType)) {
				/*
				 * Either return a Resources<Resource<T>>...
				 */
			return new Resources<>(content, doc.getUber().getLinks());
		} else {
				/*
				 * ...or return a Resources<T>
				 */

			List<Object> resourceLessContent = content.stream()
				.map(item -> (Resource<?>) item)
				.map(Resource::getContent)
				.collect(Collectors.toList());

			return new Resources<>(resourceLessContent, doc.getUber().getLinks());
		}
	}

	private static PageMetadata uberDocumentToPagingMetadata(UberDocument doc) {

		return doc.getUber().getData().stream()
			.filter(uberData -> uberData.getName() != null && uberData.getName().equals("page"))
			.findFirst()
			.map(uberData -> {
				int size = 0;
				int number = 0;
				int totalElements = 0;
				int totalPages = 0;

				for (UberData data : uberData.getData()) {
					if (data.getName().equals("size")) {
						size = (int) data.getValue();
					}

					if (data.getName().equals("number")) {
						number = (int) data.getValue();
					}

					if (data.getName().equals("totalElements")) {
						totalElements = (int) data.getValue();
					}

					if (data.getName().equals("totalPages")) {
						totalPages = (int) data.getValue();
					}
				}

				return new PageMetadata(size, number, totalElements, totalPages);
			})
			.orElse(null);
	}

	static class UberPagedResourcesDeserializer extends ContainerDeserializerBase<PagedResources<?>> implements ContextualDeserializer {

		private JavaType contentType;

		UberPagedResourcesDeserializer(JavaType contentType) {

			super(contentType);
			this.contentType = contentType;
		}

		UberPagedResourcesDeserializer() {
			this(TypeFactory.defaultInstance().constructSimpleType(UberDocument.class, new JavaType[0]));
		}

		@Override
		public PagedResources<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

			JavaType rootType = JacksonHelper.findRootType(this.contentType);

			UberDocument doc = p.getCodec().readValue(p, UberDocument.class);

			Resources<?> resources = uberDocumentToResources(doc, rootType, this.contentType);
			PageMetadata pageMetadata = uberDocumentToPagingMetadata(doc);
			return new PagedResources<>(resources.getContent(), pageMetadata, resources.getLinks());
		}

		/**
		 * Accessor for declared type of contained value elements; either exact
		 * type, or one of its supertypes.
		 */
		@Override
		public JavaType getContentType() {
			return this.contentType;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {

			if (property != null) {
				JavaType vc = property.getType().getContentType();
				return new UberPagedResourcesDeserializer(vc);
			} else {
				return new UberPagedResourcesDeserializer(ctxt.getContextualType());
			}
		}

		/**
		 * Accesor for deserializer use for deserializing content values.
		 */
		@Override
		public JsonDeserializer<Object> getContentDeserializer() {
			return null;
		}
	}

	static class UberActionDeserializer extends StdDeserializer<UberAction> {

		UberActionDeserializer() {
			super(UberAction.class);
		}

		@Override
		public UberAction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return UberAction.valueOf(p.getText().toUpperCase());
		}
	}

	public static class UberHandlerInstantiator extends HandlerInstantiator {

		private final Map<Class<?>, Object> serializers = new HashMap<>();

		public UberHandlerInstantiator() {

			this.serializers.put(UberResourceSupportSerializer.class, new UberResourceSupportSerializer());
			this.serializers.put(UberResourceSerializer.class, new UberResourceSerializer());
			this.serializers.put(UberResourcesSerializer.class, new UberResourcesSerializer());
			this.serializers.put(UberPagedResourcesSerializer.class, new UberPagedResourcesSerializer());
		}

		@Override
		public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> deserClass) {
			return (JsonDeserializer<?>) findInstance(deserClass);
		}

		@Override
		public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> keyDeserClass) {
			return (KeyDeserializer) findInstance(keyDeserClass);
		}

		@Override
		public JsonSerializer<?> serializerInstance(SerializationConfig config, Annotated annotated, Class<?> serClass) {
			return (JsonSerializer<?>) findInstance(serClass);
		}

		@Override
		public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config, Annotated annotated, Class<?> builderClass) {
			return (TypeResolverBuilder<?>) findInstance(builderClass);
		}

		@Override
		public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config, Annotated annotated, Class<?> resolverClass) {
			return (TypeIdResolver) findInstance(resolverClass);
		}

		private Object findInstance(Class<?> type) {

			Object result = this.serializers.get(type);
			return result != null ? result : BeanUtils.instantiateClass(type);
		}
	}
}
