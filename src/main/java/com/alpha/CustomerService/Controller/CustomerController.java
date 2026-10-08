package com.alpha.CustomerService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.CustomerService.Dto.CompleteRideDTO;
import com.alpha.CustomerService.Dto.CustomerDto;
import com.alpha.CustomerService.Dto.FairPriceAllVehicles;
import com.alpha.CustomerService.Dto.ResponceStructure;
import com.alpha.CustomerService.Dto.ReturningBookingObjectDto;
import com.alpha.CustomerService.Dto.RidefairDTO;
import com.alpha.CustomerService.Dto.SearchDestinationResponeDto;
import com.alpha.CustomerService.Dto.SelectRideDTO;
import com.alpha.CustomerService.Entity.Booking;
import com.alpha.CustomerService.Entity.Customer;
import com.alpha.CustomerService.Repository.BookingRepository;
import com.alpha.CustomerService.Service.CustomerService;

@RestController
//@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BookingRepository bookingRepository;

	@PostMapping("/create/createAccount")
	public ResponceStructure<Customer> createCustomer(@RequestBody CustomerDto custDto) {
		return customerService.CreateCustomer(custDto);
	}

	@DeleteMapping("/customer/deleteAccount/{custid}")
	public ResponceStructure<String> deleteCustomer(@PathVariable int custid) {
		return customerService.DeleteCustomer(custid);
	}

	@GetMapping("/customer/findcustomer/{id}")
	public ResponceStructure<Customer> findCustomer(@PathVariable int id) {
		return customerService.findCustomer(id);
	}

	@GetMapping("/customer/searchdroplocation")
	public ResponceStructure<List<SearchDestinationResponeDto>> searchdroplocation(@RequestParam String Searchkey) {
		return customerService.searchdroplocation(Searchkey);
	}

	@GetMapping("/customer/selectride")
	public ResponceStructure<FairPriceAllVehicles> selectride(@RequestBody SelectRideDTO selectRideDTO) {
		return customerService.selectRide(selectRideDTO);
	}

	@PostMapping("/customer/Booking")
	public void BookRide(@RequestParam int custId, String vehicle) {
		customerService.confirmRide(custId, vehicle);
	}

	@PutMapping("/booking/{bookingId}/assignRider/{riderId}")
	public ResponceStructure<Booking> assignRider(@PathVariable int bookingId, @PathVariable int riderId) {
		return customerService.saveRiderIdInBooking(bookingId, riderId);
	}

	@GetMapping("/customer/returningBooking")
	public ReturningBookingObjectDto getBookingObject(@RequestParam int bookingid) {
		return customerService.ReturningBookingObject(bookingid);
	}

	@PutMapping("/customer/booking/{bookingid}/status")
	public ResponceStructure<Booking> updateBookingStatus(@PathVariable int bookingid, @RequestParam String status) {
		return customerService.updateBookingStatus(bookingid, status);
	}

	@PutMapping("/customer/cancelRide")
	public void CancelRide(@RequestParam int bookingid) {
		customerService.CancelRide(bookingid);
	}

	@PutMapping("/customer/rideComplete")
	public ResponceStructure<CompleteRideDTO> completeRide(@RequestParam int bookingid) {
		return customerService.RideCompleted(bookingid);
	}

}
