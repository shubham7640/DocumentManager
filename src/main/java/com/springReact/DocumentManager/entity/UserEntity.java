package com.springReact.DocumentManager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder //Simplify Object creation by directly using builder and build method(See UserUtil class for details of implementation)
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "Users")
@JsonInclude(JsonInclude.Include.NON_DEFAULT) //the attributes with default values will not be included
public class UserEntity extends Auditable{

    @Column(updatable = false,unique = true,nullable = false)
    private String userId; //Will be randomly generated during user creation

    private String firstName;

    private String lastName;

    @Column(unique = true,nullable = false)
    private String email;

    private Integer loginAttempts;

    private LocalDateTime lastLogin;

    private String phone;

    private String bio;

    private String imageUrl;

    private boolean accountNonExpired;
    private boolean accountNonLocked;

    private boolean enabled;

    @JsonIgnore
    private String qrCodeSecret;

    @Column(columnDefinition = "text") // when we are unsure about length
    //We can use @Lob instead which will represent both Text and Longtext
    private String qrCodeSecretUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn( //joincolumn represents current entity
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( // inversejoincolumn represents entity being referenced
                    name = "role_id", referencedColumnName = "id"

            )
    )
    private RoleEntity role; //TODO : create Role class ad map here with JPA


}
