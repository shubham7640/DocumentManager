package com.springReact.DocumentManager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springReact.DocumentManager.domain.RequestContext;
import com.springReact.DocumentManager.exception.ApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
//Entity listeners are mostly used for auditing purposes
//We can provide our own Listener in argument for entity based approach which will have methods with @PostPersist, @PostRemove and @PostUpdate,etc.
// which will log the DB updates/operations performed
//Read https://prateek-ashtikar512.medium.com/spring-boot-jpa-entity-listener-fa759e5b73a9
@MappedSuperclass
//@MappedSuperclass indicates that a class is a superclass and is not associated with a specific database table,
// but its fields (or properties) can be inherited by child entity classes that are associated with tables.
@JsonIgnoreProperties(value = {"createdAt","updatedAt"}, allowGetters = true)
//Ignore these properties
//We can use @JsonIgnore at attribute level as well instead of class level representation
public abstract class Auditable {
    @Id
    @SequenceGenerator(name = "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "primary_key_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    //We can use java.util.UUID.randomUUID() for same purpose

    @NotNull
    private Long createdBy;

    @NotNull
    private Long updatedBy;

    @NotNull
    @CreatedDate //when entity was created
    @Column(name = "created_at", updatable = false,nullable = false)
    //these two lines of code each apply to different aspects of nullability:
    // While @Column(nullable = false) defines a column in the underlying database table to not be allowed to contain null values,
    // @NotNull is part of the Bean Validation specification and simply requires the createdAt attribute of any instance of Entry to not be null
    private LocalDateTime createdAt;

    @CreatedDate //when entity was created
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforePersist() {
        var userId = RequestContext.getUserId();
        if(userId==null){
            throw new ApiException("Cannot persist entity without user Id in request context for this thread");
        }
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        var userId = RequestContext.getUserId();
        if(userId==null){
            throw new ApiException("Cannot update entity without user Id in request context for this thread");
        }

        setUpdatedBy(userId);
        setUpdatedAt(LocalDateTime.now());
    }

}
