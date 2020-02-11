package com.spiralforge.ForXTransfer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.ForXTransfer.entity.Account;
import com.spiralforge.ForXTransfer.entity.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-11. Account details are fetched from database for that
	 *        particular customer.
	 * @return list of accounts.
	 */
	List<Account> findAccountByCustomer(Optional<Customer> customer);

}
