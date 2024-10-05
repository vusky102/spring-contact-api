package com.sonvu.spring_contact_api.repo;

import com.sonvu.spring_contact_api.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Mark class as repository

public interface ContactRepo extends JpaRepository<Contact, String> { // By using interface, you don't need to write logic of basic CRUD operations
    // implementation of CRUD operations is specified at runtime
    Optional<Contact> findById(String id);
}
