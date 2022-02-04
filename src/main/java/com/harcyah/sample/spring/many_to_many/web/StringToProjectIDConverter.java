package com.harcyah.sample.spring.many_to_many.web;

import com.harcyah.sample.spring.many_to_many.domain.ProjectID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToProjectIDConverter implements Converter<String, ProjectID> {

    @Override
    public ProjectID convert(String source) {
        return new ProjectID(UUID.fromString(source));
    }

}
