package com.lichi.increaselimit.common.config.serialize;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * 重写json序列化
 * null字符串返回""
 * null的Integer和Double返回-1
 * 时间返回0
 * @author majie
 *
 */
@SuppressWarnings("deprecation")
public class MyBeanSerializerModifier extends BeanSerializerModifier {

	private JsonSerializer<Object> _nullArrayJsonSerializer = new MyNullArrayJsonSerializer();
	private JsonSerializer<Object> _nullStringJsonSerializer = new MyNullStringJsonSerializer();
	private JsonSerializer<Object> _nullPrimitiveJsonSerializer = new MyNullIntegerOrDoubleJsonSerializer();
	private JsonSerializer<Object> _nullDateJsonSerializer = new MyNullDateJsonSerializer();

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		// 循环所有的beanPropertyWriter
		for (int i = 0; i < beanProperties.size(); i++) {
			BeanPropertyWriter writer = beanProperties.get(i);
			// 判断字段的类型，如果是array，list，set则注册nullSerializer
			if (isArrayType(writer)) {
				// 给writer注册一个自己的nullSerializer
				writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
			}
			if(isStringType(writer)) {
				writer.assignNullSerializer(this.defaultNullStringsonSerializer());
			}
			if(isPrimitive(writer)) {
				writer.assignNullSerializer(this.defaultNullPrimitivegsonSerializer());
			}
			if(isDate(writer)) {
				writer.assignNullSerializer(this.defaultDategsonSerializer());
			}
		}
		return beanProperties;
	}

	// 判断是什么类型
	protected boolean isArrayType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getPropertyType();
		return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
	}
	protected boolean isStringType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getPropertyType();
		return clazz.equals(String.class);
		
	}
	protected boolean isPrimitive(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getPropertyType();
		return clazz.equals(Integer.class) || clazz.equals(Double.class);
		
	}

	protected boolean isDate(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getPropertyType();
		return clazz.equals(Date.class);
		
	}

	protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
		return _nullArrayJsonSerializer;
	}
	
	protected JsonSerializer<Object> defaultNullStringsonSerializer() {
		return _nullStringJsonSerializer;
	}
	protected JsonSerializer<Object> defaultNullPrimitivegsonSerializer() {
		return _nullPrimitiveJsonSerializer;
	}
	protected JsonSerializer<Object> defaultDategsonSerializer() {
		return _nullDateJsonSerializer;
	}
	
	
}