package yo.hoo.support.mvc.mappers;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * @description 解决Date类型返回json格式为自定义格式
 * @author aokunsang
 * @date 2013-5-28
 */
@SuppressWarnings("serial")
@Component("customObjectMapper")
public class CustomObjectMapper extends ObjectMapper {

	public CustomObjectMapper() {
		super();
	    this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	    this.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
	    this.setSerializationInclusion(Include.NON_NULL);
		setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
	    AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
	    AnnotationIntrospector introspector = AnnotationIntrospector.pair(primary, secondary);
		this.setAnnotationIntrospector(introspector);
	}
}