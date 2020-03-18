package com.zhangjp.log4j2.desensitization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zhangjp.log4j2.desensitization.encry.JacksonEncry;

import java.io.Serializable;

/**
 * 金证增加银行卡出参VO 
 * 接口编码：430104
 * 
 * @author lvzhitao
 *
 */
public class AddCardOutputVo implements Serializable {

	private static final long serialVersionUID = -6930856532101893114L;
	@JsonProperty(value = "appsheetserialno")
	@JsonSerialize(using  = JacksonEncry.class)
	private String appSheetSerialNo;
	
	@JsonProperty(value = "moneyaccount")
	private String moneyAccount;
	
	@JsonProperty(value = "transactionaccountid")
	private String transactionAccountId;
	
	public String getAppSheetSerialNo() {
		return appSheetSerialNo;
	}
	public void setAppSheetSerialNo(String appSheetSerialNo) {
		this.appSheetSerialNo = appSheetSerialNo;
	}
	public String getMoneyAccount() {
		return moneyAccount;
	}
	public void setMoneyAccount(String moneyAccount) {
		this.moneyAccount = moneyAccount;
	}
	public String getTransactionAccountId() {
		return transactionAccountId;
	}
	public void setTransactionAccountId(String transactionAccountId) {
		this.transactionAccountId = transactionAccountId;
	}
	@Override
	public String toString() {
		return "AddCardOutputVo [appSheetSerialNo=" + appSheetSerialNo + ", moneyAccount=" + moneyAccount
				+ ", transactionAccountId=" + transactionAccountId + "]";
	}
	
}
