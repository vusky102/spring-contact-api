package com.sonvu.spring_contact_api.service;

import com.sonvu.spring_contact_api.domain.Contact;
import com.sonvu.spring_contact_api.repo.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.sonvu.spring_contact_api.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service // Mark class as service. This is required for @Autowired to work.
@Slf4j // Log4j2 logger. This is required for @Autowired to work.
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor //help reduce boilerplate code. e.g. Public ContactService(ContactRepo contactRepo) {
// this.contactRepo = contactRepo;

public class ContactService {
    final ContactRepo contactRepo;

    public Page<Contact> getAllContacts(int page, int size) {//help split data into pages, reducing memory usage for large data sets.
        return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }
    public Contact getContact(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new RuntimeException("Contact not found"));
    }
    public Contact createContact(Contact contact) {
        return contactRepo.save(contact);
    }
    public void updateContact(Contact contact) {
        contactRepo.save(contact);
    }
    public void deleteContact(Contact contact) {
        contactRepo.delete(contact);
    }
    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for user ID {}", id);

        Contact contact = getContact(id);
        String photoURL = photoFunction.apply(id,file);
        contact.setPhotoURL(photoURL);
        updateContact(contact);
        log.info("Photo URL generated: {}", photoURL);
        return photoURL;
    }

    private final Function<String,String> fileExtension = filename-> Optional.of(filename).filter(name-> name.contains("."))
            .map(name-> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id,image)->{
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id+fileExtension.apply(image.getOriginalFilename())), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/images/"+id+fileExtension.apply(image.getOriginalFilename())).toUriString();
        } catch (IOException e) {
            log.error("Error saving file: " + e.getMessage(), e);
            throw new RuntimeException("Unable to save image", e);
        }
    };
}
