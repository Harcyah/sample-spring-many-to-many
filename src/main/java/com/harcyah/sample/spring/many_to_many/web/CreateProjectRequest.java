package com.harcyah.sample.spring.many_to_many.web;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate start;

}
