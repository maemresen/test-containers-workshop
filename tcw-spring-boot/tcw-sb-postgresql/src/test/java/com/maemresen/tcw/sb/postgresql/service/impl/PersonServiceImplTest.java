package com.maemresen.tcw.sb.postgresql.service.impl;

import com.maemresen.tcw.sb.postgresql.repository.PersonRepository;
import com.maemresen.tcw.sb.postgresql.domain.Person;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceImplTest {

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

    @Transactional
    @Test
    public void test_findAllSuccess() {
        Set<Person> mockPersons = Set.of(
            Person.builder().name("Emre").surname("Sen").build(),
            Person.builder().name("Jack").surname("Alexander").build(),
            Person.builder().name("Martin").surname("Tylor").build(),
            Person.builder().name("Sophia").surname("Martinez").build()
        );
        personRepository.saveAll(mockPersons);

        List<Person> allPeople = personRepository.findAll();

        Assertions.assertNotNull(allPeople);
        Assertions.assertEquals(4, allPeople.size());
        mockPersons.forEach(mockPerson -> {
            String mockPersonName = mockPerson.getName();
            Assertions.assertTrue(allPeople.stream().anyMatch(foundPerson -> foundPerson.getName().equalsIgnoreCase(mockPersonName)));
        });
    }

    @Transactional
    @Test
    public void test_findByNameCaseSensitive() {
        personRepository.save(Person.builder().name("Emre").surname("Sen").build());
        Optional<Person> emre = personRepository.findTopByNameIgnoreCase("Emre");
        Assertions.assertTrue(emre.isPresent());
    }

    @Transactional
    @Test
    public void test_findByNameCaseInSensitive() {
        personRepository.save(Person.builder().name("Emre").surname("Sen").build());
        Optional<Person> emre = personRepository.findTopByNameIgnoreCase("emre");
        Assertions.assertTrue(emre.isPresent());
    }

    @Transactional
    @Test
    public void test_findByNameNotFound() {
        personRepository.save(Person.builder().name("Emre").surname("Sen").build());
        Optional<Person> emre = personRepository.findTopByNameIgnoreCase("eren");
        Assertions.assertTrue(emre.isEmpty());
    }
}