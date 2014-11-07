package yo.hoo.core.utils;

import java.lang.reflect.Method;

import javax.persistence.Entity;
import javax.persistence.Id;

public class ReflectUtil {

	public static Class<?> getWrapClass(Class<?> type) {
		if (!type.isPrimitive()) {
			return type;
		} else if (type == int.class) {
			return Integer.class;
		} else if (type == double.class) {
			return Double.class;
		} else if (type == float.class) {
			return Float.class;
		} else if (type == long.class) {
			return Long.class;
		} else if (type == short.class) {
			return Short.class;
		} else if (type == boolean.class) {
			return Boolean.class;
		} else if (type == byte.class) {
			return Byte.class;
		} else if (type == char.class) {
			return Character.class;
		}
		return type;
	}

	public static Object getId(Object bean) {
		try {
			if (bean.getClass().isAnnotationPresent(Entity.class)) {
				Method[] declaredMethods = bean.getClass().getDeclaredMethods();
				for (int i = 0; i < declaredMethods.length; i++) {
					if (declaredMethods[i].isAnnotationPresent(Id.class)) {
						return declaredMethods[i].invoke(bean);
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static String getCaption(Object bean) {
		return null;
	}

	public static String getSortId(Object bean) {
		return null;
	}

}
