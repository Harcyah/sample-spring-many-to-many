package com.harcyah.sample.spring.many_to_many.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DeveloperProject {

    public DeveloperProject(Developer developer, Project project, Role role, LocalDate created) {
        this.id = new DeveloperProjectID(developer.getId(), project.getId());
        this.developer = developer;
        this.project = project;
        this.role = role;
        this.created = created;
    }

    @EmbeddedId
    private DeveloperProjectID id;

    @ManyToOne
    @MapsId("developer")
    @JoinColumn(name = "developer_id")
    private Developer developer;

    @ManyToOne
    @MapsId("project")
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate created;

}
