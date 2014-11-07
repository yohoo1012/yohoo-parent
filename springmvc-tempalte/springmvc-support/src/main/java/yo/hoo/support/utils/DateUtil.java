package yo.hoo.support.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class DateUtil {

	public enum DateField {
		YEAR(Calendar.YEAR),
		MONTH(Calendar.MONTH),
		DAY_OF_MONTH(Calendar.DAY_OF_MONTH),
		HOUR(Calendar.HOUR),
		MINUTE(Calendar.MINUTE),
		SECOND(Calendar.SECOND),
		MILLISECOND(Calendar.MILLISECOND);

		private int field;

		private DateField(int field) {
			this.field = field;
		}

		public int getField() {
			return field;
		}
	}

	private static final SimpleDateFormat year = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat month = new SimpleDateFormat("MM");
	private static final SimpleDateFormat day = new SimpleDateFormat("dd");
	private static final SimpleDateFormat hour = new SimpleDateFormat("HH");
	private static final SimpleDateFormat minute = new SimpleDateFormat("mm");
	private static final SimpleDateFormat second = new SimpleDateFormat("ss");
	private static final SimpleDateFormat milliSecond = new SimpleDateFormat("SSS");
	private static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat yyyy_MM_dd_HH = new SimpleDateFormat("yyyy-MM-dd HH");
	private static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat yyyy_MM = new SimpleDateFormat("yyyy-MM");

	public static String format(Date date, DateField field) {
		if (date != null) {
			switch (field) {
			case YEAR:
				return year.format(date);
			case MONTH:
				return yyyy_MM.format(date);
			case DAY_OF_MONTH:
				return yyyy_MM_dd.format(date);
			case HOUR:
				return yyyy_MM_dd_HH.format(date);
			case MINUTE:
				return yyyy_MM_dd_HH_mm.format(date);
			case SECOND:
				return yyyy_MM_dd_HH_mm_ss.format(date);
			case MILLISECOND:
				return yyyy_MM_dd_HH_mm_ss_SSS.format(date);
			}
		}
		return null;
	}

	public static Date add(Date date, DateField field, int amount) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(field.getField(), amount);
		return calendar.getTime();
	}

	public static int getYear(Date date) {
		return Integer.parseInt(year.format(date));
	}

	public static int getMonth(Date date) {
		return Integer.parseInt(month.format(date));
	}

	public static int getDay(Date date) {
		return Integer.parseInt(day.format(date));
	}

	public static int getHour(Date date) {
		return Integer.parseInt(hour.format(date));
	}

	public static int getMinute(Date date) {
		return Integer.parseInt(minute.format(date));
	}

	public static int getSecond(Date date) {
		return Integer.parseInt(second.format(date));
	}

	public static int getMilliSecond(Date date) {
		return Integer.parseInt(milliSecond.format(date));
	}

	public static Date parse(String dateString) {
		try {
			if (dateString == null || "".equals(dateString)) {
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat();
			format.setLenient(false);
			if (Pattern.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3}", dateString)) {
				format.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
			} else if (Pattern.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", dateString)) {
				format.applyPattern("yyyy-MM-dd HH:mm:ss");
			} else if (Pattern.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}", dateString)) {
				format.applyPattern("yyyy-MM-dd HH:mm");
			} else if (Pattern.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}", dateString)) {
				format.applyPattern("yyyy-MM-dd HH");
			} else if (Pattern.matches("\\d{4}\\-\\d{1,2}\\-\\d{1,2}", dateString)) {
				format.applyPattern("yyyy-MM-dd");
			} else if (Pattern.matches("\\d{4}\\-\\d{1,2}", dateString)) {
				format.applyPattern("yyyy-MM");
			} else if (Pattern.matches("\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3}", dateString)) {
				format.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
			} else if (Pattern.matches("\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", dateString)) {
				format.applyPattern("yyyy/MM/dd HH:mm:ss");
			} else if (Pattern.matches("\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}", dateString)) {
				format.applyPattern("yyyy/MM/dd HH:mm");
			} else if (Pattern.matches("\\d{4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}", dateString)) {
				format.applyPattern("yyyy/MM/dd HH");
			} else if (Pattern.matches("\\d{4}\\/\\d{1,2}\\/\\d{1,2}", dateString)) {
				format.applyPattern("yyyy/MM/dd");
			} else if (Pattern.matches("\\d{4}\\/\\d{1,2}", dateString)) {
				format.applyPattern("yyyy/MM");
			} else if (Pattern.matches("\\d{4}\\d{1,2}\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}", dateString)) {
				format.applyPattern("yyyyMMdd HH:mm:ss");
			} else if (Pattern.matches("\\d{4}\\d{1,2}\\d{1,2} \\d{1,2}:\\d{1,2}", dateString)) {
				format.applyPattern("yyyyMMdd HH:mm");
			} else if (Pattern.matches("\\d{4}\\d{1,2}\\d{1,2} \\d{1,2}", dateString)) {
				format.applyPattern("yyyyMMdd HH");
			} else if (Pattern.matches("\\d{4}\\d{1,2}\\d{1,2}", dateString)) {
				format.applyPattern("yyyyMMdd");
			} else if (Pattern.matches("\\d{4}\\d{1,2}", dateString)) {
				format.applyPattern("yyyyMM");
			}
			return format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(format(parse("2014/12/2"), DateField.SECOND));
	}
	
}
