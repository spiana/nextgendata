package com.nurkiewicz.jdbcrepository.sql;

import org.springframework.data.domain.Pageable;

import com.nurkiewicz.jdbcrepository.TableDescription;

/**
 * Author: tom
 */
public class OracleSqlGenerator extends SqlGenerator {
	public OracleSqlGenerator() {
	}

	public OracleSqlGenerator(String allColumnsClause) {
		super(allColumnsClause);
	}

	@Override
	protected String limitClause(Pageable page) {
		return "";
	}

	@Override
	public String selectAll(TableDescription table, Pageable page) {
		return SQL99Helper.generateSelectAllWithPagination(table, page, this);
	}
}
