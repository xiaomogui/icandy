package com.sain.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class DateKit {
	public static final String dateFormat = "yyyy-MM-dd";
	public static final String timeFormat = "HH:mm:ss";
	public static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	/** 锁对象 */
	private static final Object lockObj = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	/**
	 * SimpleDateFormat的线程安全问题的解决方案
	 * 
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 * 
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					tl = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}

	/**
	 * 获得今日日期，时间为零点
	 * 
	 * @return
	 */
	public static Date getDateZero() {
		return getDateZero(null);
	}

	/**
	 * 设置给定日期的时间为零点，如果date为空，则返回今日零点
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateZero(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获得今日日期，时间为23:59:59
	 * 
	 * @return
	 */
	public static Date getDateLast() {
		return getDateLast(null);
	}

	/**
	 * 设置给定日期的时间为23:59:59，如果date为空，则返回今日23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateLast(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static Date getNDayBeforeCurrentDate(int n) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, n);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.MILLISECOND, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		return now.getTime();
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式化字符串
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = getSdf(pattern);
			return df.format(date);
		}
		return "";
	}

	/**
	 * 将时间对象的日期部分格化化
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		return formatDate(date, dateFormat);
	}

	/**
	 * 将时间对象的日期、时间部分格化化
	 * 
	 * @param date
	 * @return
	 */
	public static String getFullString(Date date) {
		return formatDate(date, dateTimeFormat);
	}

	/**
	 * 将时间对象的时间部分格化化
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeString(Date date) {
		return formatDate(date, timeFormat);
	}

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param aMask
	 *            输入字符串的格式
	 * @param str
	 *            一个按aMask格式排列的日期的字符串描述
	 * @return Date 对象
	 */
	public static Date parseDate(String str, String[] patterns) {
		for (String pattern : patterns) {
			try {
				SimpleDateFormat df = getSdf(pattern);
				return df.parse(str);
			} catch (Exception pe) {
			}
		}
		return null;
	}

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param aMask
	 *            输入字符串的格式
	 * @param str
	 *            一个按aMask格式排列的日期的字符串描述
	 * @return Date 对象
	 */
	public static Date parseDate(String str, String pattern) {
		try {
			SimpleDateFormat df = getSdf(pattern);
			return df.parse(str);
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * 将<code>datePattern<code>为格式的字符串解析为日期对象
	 * 
	 * @param str
	 * @return
	 */
	public static Date dateStringToDate(String str) {
		return parseDate(str, dateFormat);
	}

	/**
	 * add by fa ,2008.12.11 将日期对象转换为“yyyyMMdd”String
	 * 
	 * @param date
	 * @return String
	 */
	public static String dateToShortString(Date date) {
		SimpleDateFormat df = getSdf("yyyyMMdd");
		return df.format(date);
	}

	/**
	 * 将时间美化
	 * 
	 * @param time
	 *            格式如09:00
	 * @return 返回带凌晨，早上，中午的时间字符串 早上:6点-12点 中午:12点-14点 下午:14点-18点 晚上:18点-24点
	 *         凌晨:24点-6点
	 */
	public static String beautyTime(String time) {
		if (StringUtils.isEmpty(time))
			return time;
		String[] ss = StringUtils.split(time, ":");
		int hour = NumberUtils.toInt(ss[0]);
		if (hour >= 6 && hour < 12) {
			return "早上" + time;
		}
		if (hour >= 12 && hour < 14) {
			return "中午" + time;
		}
		if (hour >= 14 && hour < 18) {
			return "下午" + time;
		}
		if (hour >= 18 && hour < 24) {
			return "晚上" + time;
		}
		if (hour == 24 || (hour >= 0 && hour < 6)) {
			return "凌晨" + time;
		}
		return "";
	}

}
