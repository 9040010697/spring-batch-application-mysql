package com.batch.jdbc.writer;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.batch.domain.ContractHistory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BatchWriter extends JdbcBatchItemWriter<ContractHistory> {

	private static final String INSERT_QUERY_WITHOUT_UPDATE = "INSERT INTO CONTRACT_HISTORY (contract_No,"
			+ " holder_Name, duration,amount,creation_Date) "
			+ " VALUES (:contractNo, :holderName, :duration, :amount, :creationDate) ";

	private static final String DELETE_SQL = "DELETE FROM CONTRACT WHERE contract_No IN (:CONTRACT_NO)";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@PostConstruct
	void init() {
		setSql(INSERT_QUERY_WITHOUT_UPDATE);
		setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		setJdbcTemplate(jdbcTemplate);
		setAssertUpdates(false);
	}

	@Override
	public void write(final List<? extends ContractHistory> items) throws Exception {
		log.info("Writing in to History Table Record List Size :"+items.size());
		super.write(items);
		delete(items.stream().map(ContractHistory::getContractNo).collect(Collectors.toList()));
	}

	private void delete(final List<String> contractIdList) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("CONTRACT_NO", contractIdList);
		jdbcTemplate.update(DELETE_SQL, parameters);
	}
}
