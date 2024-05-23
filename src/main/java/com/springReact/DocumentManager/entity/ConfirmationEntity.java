package com.springReact.DocumentManager.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;


@Entity
@Getter
@Setter
@ToString
@Builder //Simplify Object creation
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Confirmations")
@JsonInclude(JsonInclude.Include.NON_DEFAULT) //the attributes with default values will not be included
public class ConfirmationEntity extends Auditable{
    private String key;
    @OneToOne(targetEntity = UserEntity.class, fetch= FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    //JsonIdentityInfo annotation is used in a case where there is a parent-child relationship. It is used to indicate that an object identity will be used during serialization and deserialization.
    @JsonIdentityReference(alwaysAsId = true)
    // used along with @JsonIdentityInfo to serialize an object by its id instead of whole object
    @JsonProperty("user_id")
    //Define which property of referenced entity will be associated while creating a relationship
    private UserEntity userEntity;

    public ConfirmationEntity(UserEntity userEntity) {
        this.key = UUID.randomUUID().toString();
        this.userEntity = userEntity;
    }
}
