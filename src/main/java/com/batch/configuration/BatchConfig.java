package com.batch.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.batch.domain.Contract;
import com.batch.domain.ContractHistory;
import com.batch.jdbc.writer.BatchWriter;
import com.batch.processor.BatchProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

	@Value("${app-batch.chunk-size:50}")
	private Integer chunkSize;
	
	
	
	@Bean
	public Job startBatch(JobBuilderFactory jobBuilderFactory, Step step1Jdbc) {
		return jobBuilderFactory.get("sessionDetailsJdbcJobFactory").incrementer(new RunIdIncrementer())
				.start(step1Jdbc).build();
	}

	@Bean
	public Step step1Jdbc(StepBuilderFactory stepBuilderFactory,
			ItemReader<Contract> itemReader,
			BatchWriter itemWriter, 
			BatchProcessor itemProcessor) {
		return stepBuilderFactory.get("step1Jdbc")
				.<Contract, ContractHistory> chunk(chunkSize)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
	}
	
	
	/**
	 * Reader Configuration
	 * @param dataSource
	 * @return
	 */
	@Bean
	public ItemReader<Contract> itemReader(DataSource dataSource) {
		JdbcPagingItemReader<Contract> databaseReader = new JdbcPagingItemReader<>();

		databaseReader.setDataSource(dataSource);
		databaseReader.setPageSize(chunkSize);

		PagingQueryProvider queryProvider = createQueryProvider();
		databaseReader.setQueryProvider(queryProvider);
		databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Contract.class));
		return databaseReader;
	}

	private PagingQueryProvider createQueryProvider() {
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("SELECT * ");
		queryProvider.setFromClause("FROM CONTRACT");
		queryProvider.setSortKeys(sortByCreationDate());
		return queryProvider;
	}

	private Map<String, Order> sortByCreationDate() {
		Map<String, Order> sortConfiguration = new HashMap<>();
		sortConfiguration.put("CREATION_DATE", Order.ASCENDING);
		return sortConfiguration;
	}
	
}
