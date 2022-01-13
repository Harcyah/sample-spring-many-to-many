package com.harcyah.sample.spring.many_to_many.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ProjectDTO {

    private final ProjectID id;
    private final String name;
    private final LocalDate start;
    private final Set<DeveloperID> developers;

}
