package com.harcyah.sample.spring.many_to_many.persistence;

import com.harcyah.sample.spring.many_to_many.domain.DeveloperProject;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperProjectID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperProjectRepository extends JpaRepository<DeveloperProject, DeveloperProjectID> {
}
