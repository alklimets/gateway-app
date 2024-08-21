package com.aklimets.pet.domain.dto.authentication.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserIdNumber extends DomainAttribute<String> {

    @NotNull
    private String value;

}