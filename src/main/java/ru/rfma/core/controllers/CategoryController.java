package ru.rfma.core.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.rfma.auth.service.AuthService;
import ru.rfma.core.dto.CategoryDto;
import ru.rfma.core.services.CoreServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CoreServiceImpl coreServiceImpl;
    private final AuthService authService;

    public CategoryController(CoreServiceImpl coreServiceImpl, AuthService authService) {
        this.coreServiceImpl = coreServiceImpl;
        this.authService = authService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getAll(final HttpServletRequest request) {
        final Integer userId = authService.tryGetClientIdFromRequest(request);
        final var categories = coreServiceImpl.getAllCategories(userId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> create(@RequestBody CategoryDto categoryDto, final HttpServletRequest request) {
        try {
            final Integer userId = authService.tryGetClientIdFromRequest(request);
            return new ResponseEntity<>(this.coreServiceImpl.createCategory(categoryDto.getName(), categoryDto.getSpendLimit(), userId), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byid")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> get(@RequestParam final Integer id, final HttpServletRequest request) {
        try {
            final Integer userId = authService.tryGetClientIdFromRequest(request);
            return new ResponseEntity<>(this.coreServiceImpl.getCategoryById(id, userId), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byname")
    public ResponseEntity<?> get(@RequestParam final String name, final HttpServletRequest request) {
        try {
            final Integer userId = authService.tryGetClientIdFromRequest(request);
            return new ResponseEntity<>(this.coreServiceImpl.getCategoryByName(name, userId), HttpStatus.OK);
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
