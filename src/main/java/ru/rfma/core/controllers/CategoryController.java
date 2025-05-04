package ru.rfma.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.rfma.core.dto.CategoryDto;
import ru.rfma.core.services.CoreServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CoreServiceImpl coreServiceImpl;

    public CategoryController(CoreServiceImpl coreServiceImpl) {
        this.coreServiceImpl = coreServiceImpl;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getAll() {
        final var categories = coreServiceImpl.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> create(@RequestBody CategoryDto categoryDto){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication = " + authentication);
            return new ResponseEntity<>(this.coreServiceImpl.createCategory(categoryDto.getName(), categoryDto.getSpendLimit(), 0), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byid")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> get(@RequestParam final Integer id) {
        try {
            return new ResponseEntity<>(this.coreServiceImpl.getCategoryById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byname")
    public ResponseEntity<?> get(@RequestParam final String name) {
        try {
            return new ResponseEntity<>(this.coreServiceImpl.getCategoryByName(name), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updatelimit")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> update(@RequestParam final Integer id, @RequestParam final Float spendLimit) {
        try {
            return new ResponseEntity<>(this.coreServiceImpl.updateCategoryLimit(id, spendLimit), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> delete(@RequestParam final Integer id) {
        try {
            this.coreServiceImpl.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
