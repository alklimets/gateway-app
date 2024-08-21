package com.aklimets.pet.domain.model.userprofile.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Name implements DomainAttribute<String> {

    @Column(name = "name")
    @NotNull
    private String value;

    protected Name() {
    }
}
