package com.lichi.increaselimit.common.config.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MyNullIntegerOrDoubleJsonSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
	       if (value == null) {
	    	   gen.writeObject(-1);
	        } else {
	        	gen.writeObject(value);
	        }
	}

}
