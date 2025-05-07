package ru.rfma.core.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rfma.auth.service.AuthService;
import ru.rfma.core.dto.OperationDto;
import ru.rfma.core.services.CoreServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    private CoreServiceImpl coreService;

    @Autowired
    private AuthService authService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getAll(final HttpServletRequest request) {
        final Integer userId = authService.tryGetClientIdFromRequest(request);
        final var operations = coreService.getAllOperations(userId);
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }


    @PostMapping()
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> create(@RequestBody OperationDto operationDto, final HttpServletRequest request) {
        try {
            final Integer userId = authService.tryGetClientIdFromRequest(request);
            operationDto.setUserId(userId);
            return new ResponseEntity<>(coreService.createOperation(operationDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getById(@RequestParam final Integer id, final HttpServletRequest request) {
        try {
            final Integer userId = this.authService.tryGetClientIdFromRequest(request);
            return new ResponseEntity<>(this.coreService.getOperationById(id, userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> update(@RequestBody final OperationDto operationDto, final HttpServletRequest request) {
        final Integer userId = authService.tryGetClientIdFromRequest(request);
        final var updatedOperationDto = this.coreService.updateOperation(operationDto, userId);
        return new ResponseEntity<>(updatedOperationDto, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> delete(@RequestParam final Integer id, final HttpServletRequest request) {
        try {
            final Integer userId = this.authService.tryGetClientIdFromRequest(request);
            this.coreService.deleteOperationById(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
