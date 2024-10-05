package com.sonvu.spring_contact_api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter // Getter for all fields eg. getId(), getName(), getEmail()
@Setter // Setter for all fields eg. setId(), setName(), setEmail()
@NoArgsConstructor // No args constructor e.g. new Contact()
@AllArgsConstructor // Constructor with all arguments e.g. new Contact(id, name, email),
@Table(name = "contacts") // Set Table name
//without @AllArgsConstructor, have to manually set all fields in constructor
//public Contact(String id, String name, String email, String phone, String address, String title, String status, String photoURL) {
    //super();
    //this.id = id;
    //this.name = name;
    //this.email = email;
    //this.phone = phone;
    //this.address = address;
    //this.title = title;
    //this.status = status;
    //this.photoURL = photoURL;
//}
@JsonInclude(NON_DEFAULT) // Exclude default values from JSON e.g. {"id":null,"name":null,"email":null,"phone":null,"address":null,"title":null,"status":null,"photoURL":null}
//e.g. if only id is set, then only {"id":"1"} will be returned in JSON, other fields will not be included

public class Contact {
    @Id // Set id as primary key. This is required for @Entity. If not set, then error will occur, e.g. No identifier specified for entity: com.sonvu.spring_contact_api.domain.Contact
    @UuidGenerator // Set UUID generator for id. if not set, then @Id must be set manually, or
    @Column(name = "id",unique = true, updatable = false) // one of the conditions to set id as primary key. If not set, then @Id will be generated automatically.
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String title;
    private String status;
    private String photoURL;
}
