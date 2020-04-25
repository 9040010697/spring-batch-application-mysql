package com.batch.processor;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.domain.Contract;
import com.batch.domain.ContractHistory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BatchProcessor implements ItemProcessor<Contract, ContractHistory> {

  private AtomicInteger count= new AtomicInteger();
  
  @Override
  public ContractHistory process(Contract contract) throws Exception {
	  log.info("Processing Data contractNo:"+ contract.getContractNo()+" Record No: "+ count.incrementAndGet());
		return ContractHistory.builder()
				.contractNo(contract.getContractNo())
				.duration(contract.getDuration())
				.amount(contract.getAmount())
				.holderName(contract.getHolderName())
				.creationDate(contract.getCreationDate())
				.build();
	}
}
