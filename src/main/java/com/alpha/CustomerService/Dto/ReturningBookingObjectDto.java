package com.alpha.CustomerService.Dto;
import com.alpha.CustomerService.Entity.Cordinate;

public class ReturningBookingObjectDto {
	private String destinationLoc;
	private String pickupLoc;
	private Cordinate desCordinate;
	private Cordinate sourceCordinate;
	private int riderId;
	private String bookingdate;
	private String bookingtime;
	private String dropTime;
	private double fare;
	private String status;
	
	public ReturningBookingObjectDto() {
		super();
	}

	public ReturningBookingObjectDto(String destinationLoc, String pickupLoc, Cordinate desCordinate,
			Cordinate sourceCordinate, int riderId, String bookingdate, String bookingtime, String dropTime,
			double fare, String status) {
		super();
		this.destinationLoc = destinationLoc;
		this.pickupLoc = pickupLoc;
		this.desCordinate = desCordinate;
		this.sourceCordinate = sourceCordinate;
		this.riderId = riderId;
		this.bookingdate = bookingdate;
		this.bookingtime = bookingtime;
		this.dropTime = dropTime;
		this.fare = fare;
		this.status = status;
	}

	public String getDestinationLoc() {
		return destinationLoc;
	}

	public void setDestinationLoc(String destinationLoc) {
		this.destinationLoc = destinationLoc;
	}

	public String getPickupLoc() {
		return pickupLoc;
	}

	public void setPickupLoc(String pickupLoc) {
		this.pickupLoc = pickupLoc;
	}

	public Cordinate getDesCordinate() {
		return desCordinate;
	}

	public void setDesCordinate(Cordinate desCordinate) {
		this.desCordinate = desCordinate;
	}

	public Cordinate getSourceCordinate() {
		return sourceCordinate;
	}

	public void setSourceCordinate(Cordinate sourceCordinate) {
		this.sourceCordinate = sourceCordinate;
	}

	public int getRiderId() {
		return riderId;
	}

	public void setRiderId(int riderId) {
		this.riderId = riderId;
	}

	public String getBookingdate() {
		return bookingdate;
	}

	public void setBookingdate(String bookingdate) {
		this.bookingdate = bookingdate;
	}

	public String getBookingtime() {
		return bookingtime;
	}

	public void setBookingtime(String bookingtime) {
		this.bookingtime = bookingtime;
	}

	public String getDropTime() {
		return dropTime;
	}

	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
