package com.alpha.CustomerService.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Customer customer;
	private String destinationLoc;
	private String pickupLoc;
	@OneToOne(cascade = CascadeType.ALL)
	private Cordinate desCordinate;
	@OneToOne(cascade = CascadeType.ALL)
	private Cordinate sourceCordinate;
	private String paymentType;
	private String vechicType;
	@Column(name = "rider_id")
	private int riderId;
	private String bookingdate;
	private String bookingtime;
	private String dropTime;
	private double fare;
	public Booking() {
		super();
	}
	public Booking(int id, Customer customer, String destinationLoc, String pickupLoc, Cordinate desCordinate,
			Cordinate sourceCordinate, String paymentType, String vechicType, int riderId, String bookingdate,
			String bookingtime, String dropTime, double fare) {
		super();
		this.id = id;
		this.customer = customer;
		this.destinationLoc = destinationLoc;
		this.pickupLoc = pickupLoc;
		this.desCordinate = desCordinate;
		this.sourceCordinate = sourceCordinate;
		this.paymentType = paymentType;
		this.vechicType = vechicType;
		this.riderId = riderId;
		this.bookingdate = bookingdate;
		this.bookingtime = bookingtime;
		this.dropTime = dropTime;
		this.fare = fare;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getVechicType() {
		return vechicType;
	}
	public void setVechicType(String vechicType) {
		this.vechicType = vechicType;
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
	
	
}
