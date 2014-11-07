package yo.hoo.support.utils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * jsonson utils
 * 
 * @see http://jackson.codehaus.org/
 * @see https://github.com/FasterXML/jackson
 * @see http://wiki.fasterxml.com/JacksonHome
 * @author magic_yy
 * 
 */
public class JacksonUtils {

	@SuppressWarnings("serial")
	public static class JacksonTransformerException extends RuntimeException {

		public JacksonTransformerException(Throwable e) {
			super(e);
		}

	}

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static XmlMapper xmlMapper = new XmlMapper();

	static {
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		AnnotationIntrospector introspector = AnnotationIntrospector.pair(primary, secondary);
		objectMapper.setAnnotationIntrospector(introspector);
		xmlMapper.setAnnotationIntrospector(introspector);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
			throws JacksonTransformerException {
		try {
			List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,
					new TypeReference<List<T>>() {
					});
			List<T> result = new ArrayList<T>();
			for (Map<String, Object> map : list) {
				result.add(map2pojo(map, clazz));
			}
			return result;
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	/**
	 * json string convert to map
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> json2map(String jsonStr) throws JacksonTransformerException {
		try {
			return objectMapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
			throws JacksonTransformerException {
		try {
			Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
					new TypeReference<Map<String, T>>() {
					});
			Map<String, T> result = new HashMap<String, T>();
			for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
				result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
			}
			return result;
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	/**
	 * json string convert to javaBean
	 */
	public static <T> T json2pojo(String jsonStr, Class<T> clazz) throws JacksonTransformerException {
		try {
			return objectMapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	/**
	 * json string convert to xml string
	 */
	public static String json2xml(String jsonStr) throws JacksonTransformerException {
		try {
			JsonNode root = objectMapper.readTree(jsonStr);
			String xml = xmlMapper.writeValueAsString(root);
			return xml;
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	/**
	 * map convert to javaBean
	 */
	public static <T> T map2pojo(@SuppressWarnings("rawtypes") Map map, Class<T> clazz)
			throws JacksonTransformerException {
		try {
			return objectMapper.convertValue(map, clazz);
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	/**
	 * javaBean,list,array convert to json string
	 */
	public static String obj2json(Object obj) throws JacksonTransformerException {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	public static String obj2xml(Object obj) throws JacksonTransformerException {
		try {
			try {
				return xmlMapper.writeValueAsString(obj);
			} catch (Exception e) {
				throw new JacksonTransformerException(e);
			}
		} catch (JacksonTransformerException e) {
			Throwable cause = e.getCause();
			String message = "未知错误";
			if (cause != null) {
				message = cause.getMessage();
			}
			return "<response><retCode>-101</retCode><retMessage>" + message
					+ "</retMessage><result/></response>";
		}
	}

	/**
	 * xml string convert to json string
	 */
	public static String xml2json(String xml) throws JacksonTransformerException {
		JsonParser jp = null;
		JsonGenerator jg = null;
		try {
			StringWriter w = new StringWriter();
			jp = xmlMapper.getFactory().createParser(xml);
			jg = objectMapper.getFactory().createGenerator(w);
			while (jp.nextToken() != null) {
				jg.copyCurrentEvent(jp);
			}
			return w.toString();
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		} finally {
			try {
				if (jp != null) {
					jp.close();
				}
			} catch (Exception e) {
			}
			try {
				if (jg != null) {
					jg.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * xml string convert to pojo
	 */
	public static <T> T xml2pojo(String xml, Class<T> clazz) throws JacksonTransformerException {
		try {
			JsonParser jp = xmlMapper.getFactory().createParser(xml);
			return xmlMapper.readValue(jp, clazz);
		} catch (Exception e) {
			throw new JacksonTransformerException(e);
		}
	}

	public static String jsonp2String(JSONPObject jsonpObject) {
		return jsonpObject.getFunction() + "(" + obj2json(jsonpObject.getValue()) + ")";
	}

}
