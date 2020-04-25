package com.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batch.domain.Contract;
import com.batch.repo.ContractRepo;

@RestController
@RequestMapping("/app")
public class BatchController {
	
	@Autowired
	private ContractRepo repo;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	
	@GetMapping("/insert")
	public ResponseEntity<String> insertData(){
			
		List<Contract> list = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			Contract contract = new Contract();
			contract.setContractNo(UUID.randomUUID().toString());
			contract.setHolderName("Name-"+i);
			contract.setDuration(new Random().nextInt(36));
			contract.setAmount(new Random().nextInt(500000));
			Date date= new Date();
			date.setDate(new Random().nextInt(30));
			date.setMonth(new Random().nextInt(12));
			date.setYear(new Random().nextInt(2020));
			contract.setCreationDate(date);
			
			list.add(contract);
		}
		repo.saveAll(list);
		return ResponseEntity.ok("Data Inserted.....");
	}
	
	@GetMapping("/startBatch")
	public ResponseEntity<String> startBatch() throws Exception{
		//RUN same job multiple time we are adding current milisecond to separate job steps
		JobParameters jobParameters =
				  new JobParametersBuilder()
				  .addLong("time",System.currentTimeMillis()).toJobParameters();
		
		jobLauncher.run(job, jobParameters);
		return ResponseEntity.ok("Batch Started...");
	}

}
