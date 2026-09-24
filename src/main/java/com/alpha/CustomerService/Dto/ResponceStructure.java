package com.alpha.CustomerService.Dto;

public class ResponceStructure<T>{
	private int StatusCode;
	private String message;
	private T Data;
	public ResponceStructure() {
		super();
	}
	public ResponceStructure(int statusCode, String message, T data) {
		super();
		StatusCode = statusCode;
		this.message = message;
		Data = data;
	}
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return Data;
	}
	public void setData(T data) {
		Data = data;
	}
	
	
}
