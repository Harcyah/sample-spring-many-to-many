package com.harcyah.sample.spring.many_to_many.web;

import com.harcyah.sample.spring.many_to_many.domain.Developer;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperDTO;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import com.harcyah.sample.spring.many_to_many.domain.DeveloperProject;
import com.harcyah.sample.spring.many_to_many.domain.Project;
import com.harcyah.sample.spring.many_to_many.domain.ProjectDTO;
import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import com.harcyah.sample.spring.many_to_many.persistence.DeveloperRepository;
import com.harcyah.sample.spring.many_to_many.persistence.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class AdminController {

    private final DeveloperRepository developerRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    @PostMapping("/developer")
    public void createDeveloper(@RequestBody @Valid CreateDeveloperRequest request) {
        Developer developer = new Developer(new DeveloperID(request.getId()), request.getFirstName(), request.getLastName(), new HashSet<>());
        developerRepository.save(developer);
        log.info("Created developer [{}] [{}] [{}]", developer.getId().getValue(), developer.getFirstName(), developer.getLastName());
    }

    @GetMapping("/developers")
    public List<DeveloperDTO> listDevelopers() {
        return developerRepository.findAll().stream()
            .peek(it -> log.debug("Converting developer:{}", it.getId().getValue()))
            .map(it -> new DeveloperDTO(it.getId(), it.getFirstName(), it.getLastName(), it.getProjects().stream().map(p -> p.getProject().getId()).collect(Collectors.toSet())))
            .peek(it -> log.debug("To DTO project:{}", it.getId().getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/project")
    public void createProject(@RequestBody @Valid CreateProjectRequest request) {
        Project project = new Project(new ProjectID(request.getId()), request.getName(), request.getStart(), new HashSet<>());
        projectRepository.save(project);
        log.info("Created project [{}] [{}] [{}]", project.getId().getValue(), project.getName(), project.getStart());
    }

    @GetMapping("/projects")
    public List<ProjectDTO> listProjects() {
        return projectRepository.findAll().stream()
            .peek(it -> log.debug("Converting project:{}", it.getId().getValue()))
            .map(it -> new ProjectDTO(it.getId(), it.getName(), it.getStart(), it.getDevelopers().stream().map(d -> d.getDeveloper().getId()).collect(Collectors.toSet())))
            .peek(it -> log.debug("To DTO project:{}", it.getId().getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/developer_project")
    public void createDeveloperProject(@RequestBody @Valid CreateDeveloperProjectRequest request) {
        Developer developer = developerRepository.findById(new DeveloperID(request.getDeveloperId())).orElseThrow();
        Project project = projectRepository.findById(new ProjectID(request.getProjectId())).orElseThrow();
        DeveloperProject dp = new DeveloperProject(developer, project, request.getRole(), request.getCreated());
        developer.getProjects().add(dp);
        project.getDevelopers().add(dp);
        developerRepository.save(developer);
        projectRepository.save(project);
        log.info("Created developer project [{}} [{}] [{}]", developer.getId(), project.getName(), request.getRole());
    }

}
