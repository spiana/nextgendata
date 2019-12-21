package com.nurkiewicz.jdbcrepository.sql;

import org.springframework.data.domain.Pageable;

import com.nurkiewicz.jdbcrepository.TableDescription;

/**
 * Author: tom
 */
public class MssqlSqlGenerator extends AbstractMssqlSqlGenerator {
	public MssqlSqlGenerator() {
	}

	public MssqlSqlGenerator(String allColumnsClause) {
		super(allColumnsClause);
	}


	@Override
	public String selectAll(TableDescription table, Pageable page) {
		return SQL99Helper.generateSelectAllWithPagination(table, page, this);
	}
}
