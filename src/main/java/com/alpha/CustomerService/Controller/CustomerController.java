package com.alpha.CustomerService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.CustomerService.Dto.CustomerDto;
import com.alpha.CustomerService.Dto.ResponceStructure;
import com.alpha.CustomerService.Entity.Customer;
import com.alpha.CustomerService.Service.CustomerService;

@RestController
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/create/createAccount")
	public Customer createCustomer(@RequestBody CustomerDto custDto) {
		return customerService.CreateCustomer(custDto);
	}
	
	@DeleteMapping("/customer/deleteAccount/{custid}")
	public ResponceStructure<String> deleteCustomer(@PathVariable int custid) {
		return customerService.DeleteCustomer(custid);
	}
	@GetMapping("/customer/findcustomer/{id}")
	public Customer findCustomer(@PathVariable int id) {
		return customerService.findCustomer(id);
	}
}
