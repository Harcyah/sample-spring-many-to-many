package com.harcyah.sample.spring.many_to_many.web;

import com.harcyah.sample.spring.many_to_many.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeveloperProjectRequest {

    @NotNull
    private UUID developerId;

    @NotNull
    private UUID projectId;

    @NotNull
    private Role role;

    @NotNull
    private LocalDate created;

}
