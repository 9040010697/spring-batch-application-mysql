/*package com.batch.jdbc.reader;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.stereotype.Component;

import com.batch.domain.Contract;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BatchReader extends JdbcPagingItemReader<Contract> {

	@Override
	protected void doReadPage() {
		log.info("Reading record page :" + getPage() + " and page size " + getPageSize());
		super.doReadPage();
	}

}
*/