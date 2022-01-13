package com.harcyah.sample.spring.many_to_many;

import com.harcyah.sample.spring.many_to_many.domain.Developer;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperProject;
import com.harcyah.sample.spring.many_to_many.domain.Project;
import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import com.harcyah.sample.spring.many_to_many.domain.Role;
import com.harcyah.sample.spring.many_to_many.persistence.DeveloperRepository;
import com.harcyah.sample.spring.many_to_many.persistence.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import static com.harcyah.sample.spring.many_to_many.Assertions.assertThat;

@SpringBootTest
public class SampleSpringManyToManyIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Test
    public void testRepositoriesAreDefined() {
        assertThat(projectRepository.count()).isEqualTo(0L);
        assertThat(developerRepository.count()).isEqualTo(0L);
    }

    @Test
    public void testCreate() {
        Project project1 = new Project(new ProjectID(UUID.fromString("89099607-f19a-483c-9e28-759341ac8f69")), "Project 1", LocalDate.of(2020, 10, 31), new HashSet<>());
        Project project2 = new Project(new ProjectID(UUID.fromString("f4882702-86b3-4309-a3dd-51710cbbb179")), "Project 2", LocalDate.of(2021, 12, 10), new HashSet<>());
        projectRepository.save(project1);
        projectRepository.save(project2);

        Developer developer1 = new Developer(new DeveloperID(UUID.fromString("1c4a3d8a-a52c-4d16-87df-a23b730d6517")), "Raph", "Foo", new HashSet<>());
        Developer developer2 = new Developer(new DeveloperID(UUID.fromString("5f7af7ec-fad0-4d61-bbfe-77174facc831")), "John", "Bar", new HashSet<>());
        developerRepository.save(developer1);
        developerRepository.save(developer2);

        DeveloperProject dp11 = new DeveloperProject(developer1, project1, Role.BACKEND, LocalDate.of(2021, 1, 13));
        DeveloperProject dp12 = new DeveloperProject(developer1, project2, Role.BACKEND, LocalDate.of(2017, 1, 4));
        DeveloperProject dp21 = new DeveloperProject(developer2, project1, Role.FRONTEND, LocalDate.of(2020, 8, 10));

        developer1.getProjects().add(dp11);
        developer1.getProjects().add(dp12);
        developer2.getProjects().add(dp21);
    }

}
