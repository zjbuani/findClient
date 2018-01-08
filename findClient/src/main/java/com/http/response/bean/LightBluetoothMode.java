package com.http.response.bean;

import java.io.Serializable;

public class LightBluetoothMode   {
	/**
	 * 设备mac号
	 */
	private String macNumber;
	/**
	 * 分区号
	 */
	private String areaNumber;
	/**
	 * 激活时间
	 */
	private String activateDate;
	/**
	 * 频段
	 */
	private String frequency;
	/**
	 * 温度
	 */
	private String temperature;
	/**
	 * 灯模式
	 */
	private String lightMode;

	/**
	 * 灯的档位
	 */
	private String lightGears;

	/**
	 * 主 版本号
	 */
	private String mainVersionNumber;

	/**
	 * 模块 1 版本号
	 */
	private String mode1Version;

	/**
	 * 模块 2 版本号
	 */
	private String mode2Version;

	public String getMacNumber() {
		return macNumber;
	}

	public void setMacNumber(String macNumber) {
		this.macNumber = macNumber;
	}

	public String getAreaNumber() {
		return areaNumber;
	}

	public void setAreaNumber(String areaNumber) {
		this.areaNumber = areaNumber;
	}

	public String getActivateDate() {
		return activateDate;
	}

	public void setActivateDate(String activateDate) {
		this.activateDate = activateDate;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getLightMode() {
		return lightMode;
	}

	public void setLightMode(String lightMode) {
		this.lightMode = lightMode;
	}

	public String getLightGears() {
		return lightGears;
	}

	public void setLightGears(String lightGears) {
		this.lightGears = lightGears;
	}

	public String getMainVersionNumber() {
		return mainVersionNumber;
	}

	public void setMainVersionNumber(String mainVersionNumber) {
		this.mainVersionNumber = mainVersionNumber;
	}

	public String getMode1Version() {
		return mode1Version;
	}

	public void setMode1Version(String mode1Version) {
		this.mode1Version = mode1Version;
	}

	public String getMode2Version() {
		return mode2Version;
	}

	public void setMode2Version(String mode2Version) {
		this.mode2Version = mode2Version;
	}

	@Override
	public String toString() {
		return "LightBluetoothMode [macNumber=" + macNumber + ", areaNumber=" + areaNumber + ", activateDate="
				+ activateDate + ", frequency=" + frequency + ", temperature=" + temperature + ", lightMode="
				+ lightMode + ", lightGears=" + lightGears + ", mainVersionNumber=" + mainVersionNumber
				+ ", mode1Version=" + mode1Version + ", mode2Version=" + mode2Version + "]";
	}

}
