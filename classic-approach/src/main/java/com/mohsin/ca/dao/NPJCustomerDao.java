package com.ca.dao;

import com.ca.bo.CustomerBo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class NPJCustomerDao {
    private final static String GET_CUSTOMER_BY_ID = "select id, name, dob, gender, mobile_nbr, email_address from customers where id = :id";

    private final NamedParameterJdbcTemplate npJdbcTemplate;

    public CustomerBo getCustomerById(int id) {
        final Map<String, Object> params = Map.of("id", id);
        return npJdbcTemplate.execute(GET_CUSTOMER_BY_ID, params, ps -> {
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
        });
    }
}
