package com.esoft.gascollect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.esoft.gascollect.repository")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
public class GasCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(GasCollectionApplication.class, args);
    }

}
