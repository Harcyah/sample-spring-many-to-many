package com.harcyah.sample.spring.many_to_many.web;

import com.harcyah.sample.spring.many_to_many.domain.Developer;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperDTO;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperProject;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperProjectID;
import com.harcyah.sample.spring.many_to_many.domain.Project;
import com.harcyah.sample.spring.many_to_many.domain.ProjectDTO;
import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import com.harcyah.sample.spring.many_to_many.persistence.DeveloperProjectRepository;
import com.harcyah.sample.spring.many_to_many.persistence.DeveloperRepository;
import com.harcyah.sample.spring.many_to_many.persistence.ProjectRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class AdminController {

    private final DeveloperRepository developerRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperProjectRepository developerProjectRepository;

    @Transactional
    @PostMapping("/developer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createDeveloper(@RequestBody @Valid CreateDeveloperRequest request) {
        Developer developer = new Developer(new DeveloperID(request.getId()), request.getFirstName(), request.getLastName(), new HashSet<>());
        developerRepository.save(developer);
        log.info("Created developer [{}] [{}] [{}]", developer.getId().getValue(), developer.getFirstName(), developer.getLastName());
    }

    @Transactional
    @DeleteMapping("/developer/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteDeveloper(@PathVariable DeveloperID id) {
        developerRepository.deleteById(id);
        log.info("Deleted developer [{}]", id.getValue());
    }

    @GetMapping("/developers")
    public List<DeveloperDTO> listDevelopers() {
        return developerRepository.findAll(Sort.by("firstName")).stream()
            .map(it -> new DeveloperDTO(it.getId(), it.getFirstName(), it.getLastName(), it.getProjects().stream().map(p -> p.getProject().getId()).collect(Collectors.toSet())))
            .toList();
    }

    @Transactional
    @PostMapping("/project")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createProject(@RequestBody @Valid CreateProjectRequest request) {
        Project project = new Project(new ProjectID(request.getId()), request.getName(), request.getStart(), new HashSet<>());
        projectRepository.save(project);
        log.info("Created project [{}] [{}] [{}]", project.getId().getValue(), project.getName(), project.getStart());
    }

    @Transactional
    @DeleteMapping("/project/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProject(@PathVariable ProjectID id) {
        projectRepository.deleteById(id);
        log.info("Deleted project [{}]", id.getValue());
    }

    @GetMapping("/projects")
    public List<ProjectDTO> listProjects() {
        return projectRepository.findAll(Sort.by("name")).stream()
            .map(it -> new ProjectDTO(it.getId(), it.getName(), it.getStart(), it.getDevelopers().stream().map(d -> d.getDeveloper().getId()).collect(Collectors.toSet())))
            .toList();
    }

    @Transactional
    @PostMapping("/developer_project")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createDeveloperProject(@RequestBody @Valid CreateDeveloperProjectRequest request) {
        Developer developer = developerRepository.findById(new DeveloperID(request.getDeveloperId())).orElseThrow();
        Project project = projectRepository.findById(new ProjectID(request.getProjectId())).orElseThrow();
        DeveloperProject dp = new DeveloperProject(
            new DeveloperProjectID(UUID.randomUUID()),
            developer,
            project,
            request.getRole(),
            request.getCreated()
        );
        developer.getProjects().add(dp);
        project.getDevelopers().add(dp);
        developerProjectRepository.save(dp);
        log.info("Created developer project [{}} [{}] [{}]", developer.getId(), project.getId(), request.getRole());
    }

}
