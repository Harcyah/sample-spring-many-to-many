package com.harcyah.sample.spring.many_to_many;

import com.harcyah.sample.spring.many_to_many.domain.DeveloperDTO;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import com.harcyah.sample.spring.many_to_many.domain.ProjectDTO;
import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import com.harcyah.sample.spring.many_to_many.domain.Role;
import com.harcyah.sample.spring.many_to_many.web.CreateDeveloperProjectRequest;
import com.harcyah.sample.spring.many_to_many.web.CreateDeveloperRequest;
import com.harcyah.sample.spring.many_to_many.web.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.ACCEPTED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleSpringManyToManyIntegrationTest {

    private static final UUID developerId0 = UUID.randomUUID();
    private static final UUID developerId1 = UUID.randomUUID();
    private static final UUID projectId0 = UUID.randomUUID();
    private static final UUID projectId1 = UUID.randomUUID();
    private static final UUID projectId2 = UUID.randomUUID();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateAndDelete() {
        DeveloperDTO[] developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers).hasSize(0);
        createDeveloper(developerId0, "Bob", "Morane");
        createDeveloper(developerId1, "Raph", "Zoobar");
        developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers).hasSize(2);
        assertThat(developers[0].getProjects()).isEmpty();
        assertThat(developers[1].getProjects()).isEmpty();

        ProjectDTO[] projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects).hasSize(0);
        createProject(projectId0, "Project1", LocalDate.of(2010, 10, 15));
        createProject(projectId1, "Project2", LocalDate.of(2011, 11, 15));
        createProject(projectId2, "Project3", LocalDate.of(2012, 12, 15));
        projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects).hasSize(3);
        assertThat(projects[0].getDevelopers()).isEmpty();
        assertThat(projects[1].getDevelopers()).isEmpty();
        assertThat(projects[1].getDevelopers()).isEmpty();

        createDeveloperProject(developerId0, projectId0);
        createDeveloperProject(developerId0, projectId1);
        createDeveloperProject(developerId1, projectId0);
        createDeveloperProject(developerId1, projectId1);
        createDeveloperProject(developerId1, projectId2);

        developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers).hasSize(2);
        assertThat(developers[0].getProjects()).containsExactlyInAnyOrder(new ProjectID(projectId0), new ProjectID(projectId1));
        assertThat(developers[1].getProjects()).containsExactlyInAnyOrder(new ProjectID(projectId0), new ProjectID(projectId1), new ProjectID(projectId2));

        projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects).hasSize(3);
        assertThat(projects[0].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(developerId0), new DeveloperID(developerId1));
        assertThat(projects[1].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(developerId0), new DeveloperID(developerId1));
        assertThat(projects[2].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(developerId1));

        delete("/developer", developerId0);
        delete("/project", projectId0);

        developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers).hasSize(1);
        assertThat(developers[0].getProjects()).containsExactlyInAnyOrder(new ProjectID(projectId1), new ProjectID(projectId2));

        projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects).hasSize(2);
        assertThat(projects[0].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(developerId1));
        assertThat(projects[1].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(developerId1));
    }

    private void createDeveloper(UUID id, String firstName, String lastName) {
        ResponseEntity<String> response = post("/developer", new CreateDeveloperRequest(id, firstName, lastName));
        assertThat(response.getStatusCode()).isEqualTo(ACCEPTED);
    }

    private void createProject(UUID id, String name, LocalDate date) {
        ResponseEntity<String> response = post("/project", new CreateProjectRequest(id, name, date));
        assertThat(response.getStatusCode()).isEqualTo(ACCEPTED);
    }

    private void createDeveloperProject(UUID developerId, UUID projectId) {
        ResponseEntity<String> response = post("/developer_project", new CreateDeveloperProjectRequest(developerId, projectId, Role.BACKEND, LocalDate.now()));
        assertThat(response.getStatusCode()).isEqualTo(ACCEPTED);
    }

    private ResponseEntity<String> post(String path, Object request) {
        return restTemplate.postForEntity(url(path), request, String.class);
    }

    private void delete(String path, UUID id) {
        restTemplate.delete(url(path, id));
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private String url(String path, UUID id) {
        return "http://localhost:" + port + path + "/" + id.toString();
    }

}
