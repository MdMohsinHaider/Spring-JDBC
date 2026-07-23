package com.mohsin.ca.dao;

import com.mohsin.ca.bo.CustomerBo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Repository
public class CustomerDao {
    private final static String GET_CUSTOMER_BY_ID = "select id, name, dob, gender, mobile_nbr, email_address from customers where id = ?";

    private final JdbcTemplate jdbcTemplate;

    public CustomerBo getCustomerById(int id) {
        return jdbcTemplate.execute(new GetCustomerByIdPreparedStatementCreator(id), new GetCustomerByNamePreparedStatementCallback());
    }

    @RequiredArgsConstructor
    private final class GetCustomerByIdPreparedStatementCreator implements PreparedStatementCreator {
        private final int id;

        @Override
        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
            final PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_BY_ID);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        }
    }

    private class GetCustomerByNamePreparedStatementCallback implements PreparedStatementCallback<CustomerBo> {
        @Override
        public CustomerBo doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CustomerBo(rs.getInt(1)
                        , rs.getString(2)
                        , rs.getDate(3).toLocalDate()
                        , rs.getString(4)
                        , rs.getString(5)
                        , rs.getString(6));
            }
            return null;
        }
    }

}
