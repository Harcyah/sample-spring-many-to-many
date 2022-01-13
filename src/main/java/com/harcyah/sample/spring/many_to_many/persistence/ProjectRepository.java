package com.harcyah.sample.spring.many_to_many.persistence;

import com.harcyah.sample.spring.many_to_many.domain.Project;
import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, ProjectID> {
}
