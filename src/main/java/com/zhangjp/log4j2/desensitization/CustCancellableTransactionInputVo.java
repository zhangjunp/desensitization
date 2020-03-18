package com.zhangjp.log4j2.desensitization;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 金证查询可撤单交易申请列表入参VO
 * 接口编码：430311
 * 
 * @author lvzhitao
 *
 */
public class CustCancellableTransactionInputVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "custno")
	private String custno;
	@JsonProperty(value = "fundcode")
	private String fundCode;
	@JsonProperty(value = "ismoneyfund")
	private String isMoneyFund;
	@JsonProperty(value = "pagesize")
	private String pageSize;
	@JsonProperty(value = "source")
	private String source;
	@JsonProperty(value = "startindex")
	private String startIndex;
	@JsonProperty(value = "appsheetserialno")
	private String appSheetSerialNo;
	
	
	public String getCustno() {
		return custno;
	}
	public void setCustno(String custno) {
		this.custno = custno;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getIsMoneyFund() {
		return isMoneyFund;
	}
	public void setIsMoneyFund(String isMoneyFund) {
		this.isMoneyFund = isMoneyFund;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public String getAppSheetSerialNo() {
		return appSheetSerialNo;
	}
	public void setAppSheetSerialNo(String appSheetSerialNo) {
		this.appSheetSerialNo = appSheetSerialNo;
	}
	@Override
	public String toString() {
		return "CustCancellableTransactionInputVo [custno=" + custno + ", fundCode=" + fundCode + ", isMoneyFund="
				+ isMoneyFund + ", pageSize=" + pageSize + ", source=" + source + ", startIndex=" + startIndex
				+ ", appSheetSerialNo=" + appSheetSerialNo + "]";
	}
	
}
