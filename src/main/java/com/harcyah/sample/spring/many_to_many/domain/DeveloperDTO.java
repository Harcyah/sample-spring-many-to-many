package com.harcyah.sample.spring.many_to_many.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class DeveloperDTO {

    private final DeveloperID id;
    private final String firstName;
    private final String lastName;
    private final Set<ProjectID> projects;

}
