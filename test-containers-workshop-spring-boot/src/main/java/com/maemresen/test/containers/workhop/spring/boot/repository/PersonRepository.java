package com.maemresen.test.containers.workhop.spring.boot.repository;

import com.maemresen.test.containers.workhop.spring.boot.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
