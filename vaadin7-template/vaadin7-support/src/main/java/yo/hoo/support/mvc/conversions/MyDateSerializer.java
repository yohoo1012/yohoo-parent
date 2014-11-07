package yo.hoo.support.mvc.conversions;

import java.io.IOException;
import java.util.Date;

import yo.hoo.support.utils.DateUtil;
import yo.hoo.support.utils.DateUtil.DateField;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class MyDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(DateUtil.format(value, DateField.SECOND));
	}
	
}