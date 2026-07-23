package com.mohsin.qba.main;

import com.mohsin.qba.config.PersistenceConfig;
import com.mohsin.qba.dao.CustomerDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class QBAApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        CustomerDao customerDao = context.getBean("customerDao", CustomerDao.class);

        /*long count = customerDao.count();
        System.out.println("count of customers: " + count);*/

        /*String customerName = customerDao.findCustomerNmById(1);
        System.out.println("customer name : " + customerName);*/

        /*CustomerDao.CustomerBo bo = customerDao.findCustomerById(1);
        System.out.println(bo);*/
        /*CustomerDao.CustomerBo bo = new CustomerDao.CustomerBo(5, "sameer", LocalDate.of(2000, 02, 10), "Male", "9383877444", "sameer@gmail.com");
        int id = customerDao.saveAndGetId(bo);
        System.out.println("Saved Customer with id " + id);

        customerDao.findCustomersByName("sam").forEach(System.out::println);*/
        CustomerDao.CustomerBo customerBo = customerDao.findCustomerOrdersById(2);
        System.out.println(customerBo);
    }
}
