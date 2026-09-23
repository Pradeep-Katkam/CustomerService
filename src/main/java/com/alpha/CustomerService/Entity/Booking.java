package com.alpha.CustomerService.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
	@ManyToOne
	private Customer customer;
	@OneToOne
	private Address destinationLoc;
	@OneToOne
	private Address pickupLoc;
	private String PaymentType;
	private String vechicType;
	private int riderId;
	private String bookingdate;
	private String bookingtime;
	private String dropTime;
	private double fare;
	public Booking() {
		super();
	}
	public Booking(int id, Customer customer, Address destinationLoc, Address pickupLoc, String paymentType,
			String vechicType, int riderId, String bookingdate, String bookingtime, String dropTime, double fare) {
		super();
		this.id = id;
		this.customer = customer;
		this.destinationLoc = destinationLoc;
		this.pickupLoc = pickupLoc;
		PaymentType = paymentType;
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
	public Address getDestinationLoc() {
		return destinationLoc;
	}
	public void setDestinationLoc(Address destinationLoc) {
		this.destinationLoc = destinationLoc;
	}
	public Address getPickupLoc() {
		return pickupLoc;
	}
	public void setPickupLoc(Address pickupLoc) {
		this.pickupLoc = pickupLoc;
	}
	public String getPaymentType() {
		return PaymentType;
	}
	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
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
