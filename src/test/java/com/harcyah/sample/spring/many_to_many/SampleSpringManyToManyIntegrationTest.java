package com.harcyah.sample.spring.many_to_many;

import com.harcyah.sample.spring.many_to_many.domain.DeveloperDTO;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import com.harcyah.sample.spring.many_to_many.domain.ProjectDTO;
import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import com.harcyah.sample.spring.many_to_many.domain.Role;
import com.harcyah.sample.spring.many_to_many.web.CreateDeveloperProjectRequest;
import com.harcyah.sample.spring.many_to_many.web.CreateDeveloperRequest;
import com.harcyah.sample.spring.many_to_many.web.CreateProjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.ACCEPTED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleSpringManyToManyIntegrationTest {

    private static final UUID DEVELOPER_ID_0 = UUID.randomUUID();
    private static final UUID DEVELOPER_ID_1 = UUID.randomUUID();
    private static final UUID PROJECT_ID_0 = UUID.randomUUID();
    private static final UUID PROJECT_ID_1 = UUID.randomUUID();
    private static final UUID PROJECT_ID_2 = UUID.randomUUID();

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUpDevelopers() {
        DeveloperDTO[] developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers).isEmpty();
        createDeveloper(DEVELOPER_ID_0, "Bob", "Morane");
        createDeveloper(DEVELOPER_ID_1, "Raph", "Zoobar");
        developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers)
            .isNotNull()
            .hasSize(2);
        assertThat(developers[0]).isNotNull();
        assertThat(developers[0].getProjects()).isEmpty();
        assertThat(developers[1]).isNotNull();
        assertThat(developers[1].getProjects()).isEmpty();
    }

    @BeforeEach
    void setUpProjects() {
        ProjectDTO[] projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects).isEmpty();
        createProject(PROJECT_ID_0, "Project1", LocalDate.of(2010, 10, 15));
        createProject(PROJECT_ID_1, "Project2", LocalDate.of(2011, 11, 15));
        createProject(PROJECT_ID_2, "Project3", LocalDate.of(2012, 12, 15));
        projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects)
            .isNotNull()
            .hasSize(3);
        assertThat(projects[0]).isNotNull();
        assertThat(projects[0].getDevelopers()).isNotNull().isEmpty();
        assertThat(projects[1]).isNotNull();
        assertThat(projects[1].getDevelopers()).isNotNull().isEmpty();
        assertThat(projects[2]).isNotNull();
        assertThat(projects[2].getDevelopers()).isNotNull().isEmpty();
    }

    @Test
    void testCreateAndDelete() {
        createDeveloperProject(DEVELOPER_ID_0, PROJECT_ID_0);
        createDeveloperProject(DEVELOPER_ID_0, PROJECT_ID_1);
        createDeveloperProject(DEVELOPER_ID_1, PROJECT_ID_0);
        createDeveloperProject(DEVELOPER_ID_1, PROJECT_ID_1);
        createDeveloperProject(DEVELOPER_ID_1, PROJECT_ID_2);

        DeveloperDTO[] developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers)
            .isNotNull()
            .hasSize(2);
        assertThat(developers[0].getProjects()).containsExactlyInAnyOrder(new ProjectID(PROJECT_ID_0), new ProjectID(PROJECT_ID_1));
        assertThat(developers[1].getProjects()).containsExactlyInAnyOrder(new ProjectID(PROJECT_ID_0), new ProjectID(PROJECT_ID_1), new ProjectID(PROJECT_ID_2));

        ProjectDTO[] projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects)
            .isNotNull()
            .hasSize(3);
        assertThat(projects[0].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(DEVELOPER_ID_0), new DeveloperID(DEVELOPER_ID_1));
        assertThat(projects[1].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(DEVELOPER_ID_0), new DeveloperID(DEVELOPER_ID_1));
        assertThat(projects[2].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(DEVELOPER_ID_1));

        delete("/developer", DEVELOPER_ID_0);
        delete("/project", PROJECT_ID_0);

        developers = restTemplate.getForObject(url("/developers"), DeveloperDTO[].class);
        assertThat(developers)
            .isNotNull()
            .hasSize(1);
        assertThat(developers[0].getProjects()).containsExactlyInAnyOrder(new ProjectID(PROJECT_ID_1), new ProjectID(PROJECT_ID_2));

        projects = restTemplate.getForObject(url("/projects"), ProjectDTO[].class);
        assertThat(projects)
            .isNotNull()
            .hasSize(2);
        assertThat(projects[0].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(DEVELOPER_ID_1));
        assertThat(projects[1].getDevelopers()).containsExactlyInAnyOrder(new DeveloperID(DEVELOPER_ID_1));
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
