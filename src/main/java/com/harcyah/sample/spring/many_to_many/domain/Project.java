package com.harcyah.sample.spring.many_to_many.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private ProjectID id;

    private String name;
    private LocalDate start;

    @OneToMany(mappedBy = "project", fetch = EAGER, orphanRemoval = true)
    private Set<DeveloperProject> developers = new HashSet<>();

}
