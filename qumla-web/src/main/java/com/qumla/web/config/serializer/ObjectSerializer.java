package com.qumla.web.config.serializer;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;

public class ObjectSerializer extends Serializer{

	@Override
	public void serilaize(JsonGenerator jgen, PropertyDescriptor pd, Object v,
			List<String> names) throws IOException {
		jgen.writeObjectField(pd.getName(), v);
		names.add(pd.getName());
	}

}
