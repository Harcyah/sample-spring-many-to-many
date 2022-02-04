package com.harcyah.sample.spring.many_to_many.web;

import com.harcyah.sample.spring.many_to_many.domain.DeveloperID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToDeveloperIDConverter implements Converter<String, DeveloperID> {

    @Override
    public DeveloperID convert(String source) {
        return new DeveloperID(UUID.fromString(source));
    }

}
