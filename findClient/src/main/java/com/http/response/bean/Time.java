package com.http.response.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

	/**
	 * 由过去的某一时间,计算距离当前的时间
	 */
	public String CalculateTime(String time) {
		long nowTime = System.currentTimeMillis(); // 获取当前时间的毫秒数
		String msg = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 指定时间格式
		Date setTime = null; // 指定时间
		try {
			setTime = sdf.parse(time); // 将字符串转换为指定的时间格式
		} catch (ParseException e) {

			e.printStackTrace();
		}

		long reset = setTime.getTime(); // 获取指定时间的毫秒数
		long dateDiff = nowTime - reset;

		if (dateDiff < 0) {
			msg = "输入的时间不对";
		} else {

			long dateTemp1 = dateDiff / 1000; // 秒
			long dateTemp2 = dateTemp1 / 60; // 分钟
			long dateTemp3 = dateTemp2 / 60; // 小时
			long dateTemp4 = dateTemp3 / 24; // 天数
			long dateTemp5 = dateTemp4 / 30; // 月数
			long dateTemp6 = dateTemp5 / 12; // 年数

			if (dateTemp6 > 0) {
				msg = dateTemp6 + "年前";

			} else if (dateTemp5 > 0) {
				msg = dateTemp5 + "个月前";
				msg = dateTemp5*30*24+ "h";
			} else if (dateTemp4 > 0) {
				msg = dateTemp4 + "天前";
				msg = dateTemp4 * 24 + "h";
				System.out.println("天");
			} else if (dateTemp3 > 0) {
				msg = dateTemp3 + "小时前";
				System.out.println("时");
				msg = dateTemp3 + "h";
			} else if (dateTemp2 > 0) {
				System.out.println("分" +dateTemp2);
				msg = dateTemp2 + "分钟前";
				msg = dateTemp2 / 60 + "h";
				System.out.println("msg 分" +msg);
			} else if (dateTemp1 > 0) {
				System.out.println("dateTemp1  "  +dateTemp1);
				msg = "刚刚";
				msg = dateTemp1/60/60 + "h";
			}
		}
		return msg;

	}
}