package com.maemresen.tcw.sb.postgresql.service.impl;

import com.maemresen.tcw.sb.postgresql.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseAccessibilityTest {

    @ClassRule
    public static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.1")
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa");

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void test1_databaseDrop() {
        Assertions.assertDoesNotThrow(() -> personRepository.findAll());
        postgreSQLContainer.stop();
        Assertions.assertThrows(Exception.class, () -> personRepository.findAll());
    }
}