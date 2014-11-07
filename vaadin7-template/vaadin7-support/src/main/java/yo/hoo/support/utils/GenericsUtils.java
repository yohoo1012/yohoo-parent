package yo.hoo.support.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 泛型工具类
 * 
 * @author Johnson
 * @version Tuesday October 26th, 2010
 */
public class GenericsUtils {
	private static final Log log = LogFactory.getLog(GenericsUtils.class);

	/**
	 * 通过反射获得定义Class时声明的父类的范型参数的类型
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenericType(Class<?> clazz) {
		return GenericsUtils.getSuperClassGenericType(clazz, 0);
	}

	/**
	 * 通过反射获得定义Class时声明的父类的范型参数的类型
	 * 
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class<?> getSuperClassGenericType(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			log.info(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			log.info("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.info(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class<?>) params[index];
	}
}