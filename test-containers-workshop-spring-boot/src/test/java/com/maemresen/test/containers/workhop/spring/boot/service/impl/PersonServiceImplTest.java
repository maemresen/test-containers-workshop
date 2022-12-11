package com.maemresen.test.containers.workhop.spring.boot.service.impl;

import com.maemresen.test.containers.workhop.spring.boot.domain.Person;
import com.maemresen.test.containers.workhop.spring.boot.repository.PersonRepository;
import java.util.List;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = PersonServiceImplTest.Initializer.class)
public class PersonServiceImplTest {

    @ClassRule
    public static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.1")
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                "spring.jpa.hibernate.ddl-auto=create"
            ).applyTo(context.getEnvironment());
        }
    }

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    @Test
    public void test_findAllSuccess() {
        personRepository.save(Person.builder().name("Emre").surname("Sen").build());
        personRepository.save(Person.builder().name("Jack").surname("Alexander").build());
        personRepository.save(Person.builder().name("Martin").surname("Tylor").build());
        personRepository.save(Person.builder().name("Sophia").surname("Martinez").build());

        List<Person> allPeople = personRepository.findAll();

        Assertions.assertNotNull(allPeople);
        Assertions.assertEquals(4, allPeople.size());
    }

    @Test
    public void test_findAllDatabaseUnreachable() {
        postgreSQLContainer.stop();
        Assertions.assertThrows(Throwable.class, personRepository::findAll);
    }
}