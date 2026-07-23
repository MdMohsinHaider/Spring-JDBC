package com.ca.main;

import com.ca.bo.CustomerBo;
import com.ca.config.PersistenceConfig;
import com.ca.dao.CustomerDao;
import com.ca.dao.NPJCustomerDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CAApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        NPJCustomerDao customerDao = applicationContext.getBean(NPJCustomerDao.class);
        CustomerBo customerBo = customerDao.getCustomerById(1);
        System.out.println(customerBo);
    }
}
