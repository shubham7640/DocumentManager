package com.springReact.DocumentManager.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springReact.DocumentManager.enumeration.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder //Simplify Object creation
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Roles")
@JsonInclude(JsonInclude.Include.NON_DEFAULT) //the attributes with default values will not be included

public class RoleEntity extends Auditable{

    private String roleName;

    private Authority authorites;
    //TODO  : ENUM required
}
