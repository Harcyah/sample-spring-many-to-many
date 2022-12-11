package com.harcyah.sample.spring.many_to_many.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "value", column = @Column(name = "id"))
public class DeveloperID implements Serializable {

    private UUID value;

}
