package com.alpha.CustomerService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alpha.CustomerService.Dto.ResponceStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomerNotExist.class)
	public void CustomrNotExistingException() {
		ResponceStructure<String> rs = new ResponceStructure<String>();
		rs.setStatusCode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Customer Not Exist With Id");
		rs.setData("Customer Not Found");
	}
}
