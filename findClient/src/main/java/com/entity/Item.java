package com.entity;

public class Item {
	private String misName;
	private int curLoad;
	private String misCode;
	private int misId;
	private String requireAmount;
	private long requireDtime;

	public String getMisName() {
		return misName;
	}

	public void setMisName(String misName) {
		this.misName = misName;
	}

	public int getCurLoad() {
		return curLoad;
	}

	public void setCurLoad(int curLoad) {
		this.curLoad = curLoad;
	}

	public String getMisCode() {
		return misCode;
	}

	public void setMisCode(String misCode) {
		this.misCode = misCode;
	}

	public int getMisId() {
		return misId;
	}

	public void setMisId(int misId) {
		this.misId = misId;
	}

	public String getRequireAmount() {
		return requireAmount;
	}

	public void setRequireAmount(String requireAmount) {
		this.requireAmount = requireAmount;
	}

	public long getRequireDtime() {
		return requireDtime;
	}

	public void setRequireDtime(long requireDtime) {
		this.requireDtime = requireDtime;
	}

	

}
