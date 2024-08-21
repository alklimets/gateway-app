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
public class City implements DomainAttribute<String> {

    @Column(name = "city")
    @NotNull
    private String value;

    protected City() {
    }
}
