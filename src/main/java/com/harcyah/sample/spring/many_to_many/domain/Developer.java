package com.harcyah.sample.spring.many_to_many.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Developer {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private DeveloperID id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "developer", fetch = FetchType.EAGER)
    private Set<DeveloperProject> projects = new HashSet<>();

}
