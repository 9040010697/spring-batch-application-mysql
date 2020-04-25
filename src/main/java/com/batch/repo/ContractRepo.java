package com.batch.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.domain.Contract;

@Repository
public interface ContractRepo extends JpaRepository<Contract, String>{

}
