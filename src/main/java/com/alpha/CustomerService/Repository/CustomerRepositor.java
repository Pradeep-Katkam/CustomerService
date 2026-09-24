package com.alpha.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.CustomerService.Entity.Customer;

public interface CustomerRepositor extends JpaRepository<Customer, Integer>{

}
