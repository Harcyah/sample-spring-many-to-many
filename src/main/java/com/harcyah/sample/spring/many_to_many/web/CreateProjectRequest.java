package com.harcyah.sample.spring.many_to_many.web;

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
public class CreateProjectRequest {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate start;

}
