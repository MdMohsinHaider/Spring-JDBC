package com.mohsin.jca.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class CustomerDao {
    private final static String findCustomerNameById = "select name from customers where id = :id";
    private final static String countCustomers = "select count(*) from customers";
    private final static String findById = "select id, name, dob, gender, mobile_nbr, email_address from customers where id = :id";
    private final static String findAllCustomers = "select id, name, dob, gender, mobile_nbr, email_address from customers";
    private final static String insertCustomer = "insert into customers(name, dob, gender, mobile_nbr, email_address) values (:name, :dob, :gender, :mobileNo, :emailAddress)";

    public record CustomerBo(int id, String name, LocalDate dob, String gender, String mobileNo, String emailAddress) {
    }

    private final RowMapper<CustomerBo> customerBoRowMapper = (rs, rowNum) -> {
        return new CustomerBo(rs.getInt(1),
                rs.getString(2),
                rs.getDate(3).toLocalDate(),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6));
    };

    private final JdbcClient jdbcClient;

    @NonNull
    public String findCustomerNameById(final int id) {
        return jdbcClient.sql(findCustomerNameById)
                .param("id", id)
                .query(String.class)
                .single();
    }

    @NonNull
    public long countCustomers() {
        return jdbcClient.sql(countCustomers)
                .query(Long.class)
                .single();
    }

    public CustomerBo findById(final int id) {
        return jdbcClient.sql(findById)
                .param("id", id)
                .query(customerBoRowMapper)
                .single();
    }

    public List<CustomerBo> findAllCustomers() {
        return jdbcClient.sql(findAllCustomers)
                .query(customerBoRowMapper)
                .list();
    }

    public int saveCustomer(final CustomerBo customerBo) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(insertCustomer)
                .params(Map.of("name", customerBo.name(), "dob", customerBo.dob(), "gender", customerBo.gender()
                , "mobileNo", customerBo.mobileNo(), "emailAddress", customerBo.emailAddress()))
                .update(keyHolder, new String[]{"id"});
        return keyHolder.getKey().intValue();
    }


}
