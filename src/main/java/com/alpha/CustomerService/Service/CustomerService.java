package com.alpha.CustomerService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alpha.CustomerService.Dto.CustomerDto;
import com.alpha.CustomerService.Dto.ResponceStructure;
import com.alpha.CustomerService.Entity.Customer;
import com.alpha.CustomerService.Exception.CustomerNotExist;
import com.alpha.CustomerService.Repository.CustomerRepositor;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepositor customerRepositor;
	
	public Customer CreateCustomer(CustomerDto custDto) {
		Customer c = new Customer();
		c.setName(custDto.getName());
		c.setMobile(custDto.getMobile());
		c.setEmail(custDto.getEmail());
		c.setGender(custDto.getGender());
		Customer cust =customerRepositor.save(c);
		
		int custId=cust.getId();

		if(custId>=1 && custId <=9999) {
			String otp = String.format("%04d", custId);
			System.out.println(otp);
			c.setOtp(otp);
		}else if(custId>=10000){
			System.out.println(custId%10000);
			c.setOtp(custId%10000+"");
		}
		customerRepositor.save(c);
		return c;
	}

	public ResponceStructure<String> DeleteCustomer(int custid) {
		Customer cust=customerRepositor.findById(custid).orElseThrow(()-> new CustomerNotExist());
		customerRepositor.deleteById(custid);
		
		ResponceStructure<String> rs = new ResponceStructure<String>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("Delete Succesfully");
		rs.setData("Deleted record");
		return rs;
	}

	public Customer findCustomer(int id) {
		Customer cust=customerRepositor.findById(id).orElseThrow(()-> new CustomerNotExist());
		return cust;
	}

}
