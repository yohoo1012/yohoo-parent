package yo.hoo.support.utils;

import java.io.Serializable;
import java.util.List;

public class StringUtils {

	public static boolean isEmpty(String string) {
		return string == null
				|| string.length() == 0;
	}

	public static boolean isEmptyText(String string) {
		return string == null
				|| string.trim().length() == 0;
	}

	public static boolean isEmpty(Serializable[] ids) {
		return ids == null
				|| ids.length == 0;
	}

	public static boolean isEmpty(List<?> list) {
		return list == null
				|| list.isEmpty();
	}

	public static Object substr(StringBuffer buf) {
		return (buf == null
				|| buf.length() <= 1 ? "" : buf.substring(0, buf.length() - 1));
	}

}