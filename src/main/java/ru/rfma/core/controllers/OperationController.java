package ru.rfma.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rfma.core.dto.OperationDto;
import ru.rfma.core.services.CoreServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    private CoreServiceImpl coreService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(coreService.getAllOperations(), HttpStatus.OK);
    }


    @PostMapping()
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> create(@RequestBody OperationDto operationDto) {
        try {
            return new ResponseEntity<>(coreService.createOperation(operationDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getById(@RequestParam final Integer id) {
        try {
            return new ResponseEntity<>(this.coreService.getOperationById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> delete(@RequestParam final Integer id) {
        try {
            this.coreService.deleteOperationById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
