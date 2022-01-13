package com.harcyah.sample.spring.many_to_many.persistence;

import com.harcyah.sample.spring.many_to_many.domain.Developer;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, DeveloperID> {
}
