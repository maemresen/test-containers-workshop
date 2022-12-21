package com.maemresen.test.containers.workhop.spring.boot.service.impl;

import com.maemresen.test.containers.workhop.spring.boot.domain.Person;
import com.maemresen.test.containers.workhop.spring.boot.repository.PersonRepository;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonServiceImplTest {

    @Autowired
    PersonRepository personRepository;

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

//    @Test
//    public void test_findAllDatabaseUnreachable() {
//        postgreSQLContainer.stop();
//        Assertions.assertThrows(Throwable.class, personRepository::findAll);
//    }
}