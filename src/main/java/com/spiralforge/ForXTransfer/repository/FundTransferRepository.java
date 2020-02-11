package com.spiralforge.ForXTransfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiralforge.ForXTransfer.entity.FundTransfer;

@Repository
public interface FundTransferRepository extends JpaRepository<FundTransfer, Long>{

	List<FundTransfer> findAllByTransferStatus(String statusPendingMessage);

}
