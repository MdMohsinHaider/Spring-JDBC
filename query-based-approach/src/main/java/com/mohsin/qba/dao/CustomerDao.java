package com.mohsin.qba.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerDao {
    public record CustomerBo(int id, String name, LocalDate dob, String gender, String mobileNo, String emailAddress,
                             List<OrderBo> orders) {
    }

    public record OrderBo(int id, LocalDate orderDate, double amount, String orderStatus,
                          List<OrderItemsBo> orderItems) {
    }

    public record OrderItemsBo(int id, String productName, int quantity, double unitPrice) {
    }

    /*private final RowMapper<CustomerBo> customerBoRowMapper = (rs, rowNum) -> new CustomerBo(rs.getInt(1),
            rs.getString(2),
            rs.getDate(3).toLocalDate(),
            rs.getString(4),
            rs.getString(5),
            rs.getString(6));*/

    private final static String SQL_COUNT_CUSTOMERS = "select count(1) from customers";
    private final static String SQL_FIND_CUSTOMER_NM_BY_ID = "select name from customers where id=:id";
    private final static String SQL_FIND_CUSTOMER_BY_ID = "select id, name, dob, gender, mobile_nbr, email_address from customers where id=:id";
    private final static String SQL_FIND_CUSTOMERS_BY_NAME = "select id, name, dob, gender, mobile_nbr, email_address from customers where name like :name";
    private final static String SQL_INSERT_CUSTOMER = "insert into customers(name, dob, gender, mobile_nbr, email_address) values (:name, :dob, :gender, :mobileNo, :emailAddress)";
    private final static String SQL_GET_CUSTOMER_ORDERS_BY_ID = "select c.id, c.name, c.dob, c.gender, c.mobile_nbr, c.email_address, o.id, o.order_dt, o.amount, o.status, oi.id, oi.product_nm, oi.quantity, oi.unit_price from customers c inner join orders o on c.id = o.customer_id inner join order_items oi on o.id = oi.order_id where c.id=:id order by o.id, o.order_dt";

    private final NamedParameterJdbcTemplate npJdbcTemplate;

    public long count() {
        return npJdbcTemplate.queryForObject(SQL_COUNT_CUSTOMERS, Map.of(), Long.class);
    }

    public String findCustomerNmById(final int id) {
        //return npJdbcTemplate.queryForObject(SQL_FIND_CUSTOMER_NM_BY_ID, Map.of("id", id), String.class);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return npJdbcTemplate.queryForObject(SQL_FIND_CUSTOMER_NM_BY_ID, params, String.class);
    }

    /*public CustomerBo findCustomerById(final int id) {
        return npJdbcTemplate.queryForObject(SQL_FIND_CUSTOMER_BY_ID,
                new MapSqlParameterSource("id", id), customerBoRowMapper);
    }

    public List<CustomerBo> findCustomersByName(final String name) {
        return npJdbcTemplate.query(SQL_FIND_CUSTOMERS_BY_NAME, new MapSqlParameterSource("name", "%" + name + "%"), customerBoRowMapper);
    }*/

    public int saveCustomer(final CustomerBo customerBo) {
        return npJdbcTemplate.update(SQL_INSERT_CUSTOMER, new BeanPropertySqlParameterSource(customerBo));
    }

    public int saveAndGetId(final CustomerBo customerBo) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(customerBo);
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        npJdbcTemplate.update(SQL_INSERT_CUSTOMER, params, keyHolder, new String[]{"id"});
        return keyHolder.getKey().intValue();
    }

    public CustomerBo findCustomerOrdersById(final int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return npJdbcTemplate.query(SQL_GET_CUSTOMER_ORDERS_BY_ID, params, (rs) -> {
            CustomerBo customerBo = null;
            OrderBo orderBo = null;
            OrderItemsBo orderItemsBo = null;
            Map<Integer, CustomerBo> customerMap = new HashMap<Integer, CustomerBo>();
            Map<Integer, OrderBo> orders = new HashMap<Integer, OrderBo>();

            while (rs.next()) {
                int customerId = rs.getInt(1);
                int orderId = rs.getInt(7);

                if (customerMap.containsKey(customerId)) {
                    customerBo = customerMap.get(customerId);
                } else {
                    customerBo = new CustomerBo(rs.getInt(1),
                            rs.getString(2),
                            rs.getDate(3).toLocalDate(),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6), new ArrayList<>());
                    customerMap.put(customerId, customerBo);
                }
                if (orders.containsKey(orderId)) {
                    orderBo = orders.get(orderId);
                } else {
                    orderBo = new OrderBo(rs.getInt(7), rs.getDate(8).toLocalDate(), rs.getDouble(9), rs.getString(10), new ArrayList<>());
                    orders.put(orderId, orderBo);
                    customerBo.orders().add(orderBo);
                }
                orderItemsBo = new OrderItemsBo(rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getDouble(14));
                orderBo.orderItems().add(orderItemsBo);
            }
            return customerMap.values().stream().toList();
        }).get(0);
    }


}
