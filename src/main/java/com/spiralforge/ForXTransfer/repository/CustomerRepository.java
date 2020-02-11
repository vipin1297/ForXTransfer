package com.spiralforge.ForXTransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.ForXTransfer.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Customer findByMobileNumberAndPassword(Long mobileNumber, String password);

}
