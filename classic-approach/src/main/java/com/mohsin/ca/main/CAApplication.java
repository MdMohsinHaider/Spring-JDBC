package com.mohsin.ca.main;

import com.mohsin.ca.bo.CustomerBo;
import com.mohsin.ca.config.PersistenceConfig;
import com.mohsin.ca.dao.NPJCustomerDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CAApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        NPJCustomerDao customerDao = applicationContext.getBean(NPJCustomerDao.class);
        CustomerBo customerBo = customerDao.getCustomerById(2);
        System.out.println(customerBo);
    }
}
