package com.mohsin.jca.main;

import com.mohsin.jca.config.PersistenceConfig;
import com.mohsin.jca.dao.CustomerDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class JCAApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        CustomerDao customerDao = context.getBean(CustomerDao.class);
        /*String customerName = customerDao.findCustomerNameById(1);
        System.out.println("customer name : " + customerName);*/

        /*long c = customerDao.countCustomers();
        System.out.println("count of customers = " + c);*/
        /*CustomerDao.CustomerBo bo = customerDao.findById(101);
        System.out.println(bo);*/
        /*customerDao.findAllCustomers().forEach(System.out::println);*/
        CustomerDao.CustomerBo customer = new CustomerDao.CustomerBo(
                107,
                "Ria Thompson",
                LocalDate.of(1992, 4, 18),
                "F",
                "4045551287",
                "ava.thompson92@example.com"
        );
        int id = customerDao.saveCustomer(customer);
        System.out.println("Saved Customer with id " + id);

    }
}
