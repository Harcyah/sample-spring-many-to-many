package com.harcyah.sample.spring.many_to_many.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeveloperRequest {

    @NotNull
    private UUID id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

}
