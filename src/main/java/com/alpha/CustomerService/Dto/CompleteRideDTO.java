package com.alpha.CustomerService.Dto;

public class CompleteRideDTO {
	private int rideid;
	private String status;
	private String payment;
	private String dropTiming;
	public CompleteRideDTO(String status, String payment, String dropTiming, int rideid) {
		super();
		this.rideid = rideid;
		this.status = status;
		this.payment = payment;
		this.dropTiming = dropTiming;
	}
	public int getRideid() {
		return rideid;
	}
	public void setRideid(int rideid) {
		this.rideid = rideid;
	}
	public CompleteRideDTO() {
		super();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getDropTiming() {
		return dropTiming;
	}
	public void setDropTiming(String dropTiming) {
		this.dropTiming = dropTiming;
	}
	
}
