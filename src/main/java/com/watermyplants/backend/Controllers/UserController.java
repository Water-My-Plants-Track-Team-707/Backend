package com.watermyplants.backend.Controllers;

import com.watermyplants.backend.Models.User;
import com.watermyplants.backend.Models.UserMinimum;
import com.watermyplants.backend.Services.UserServices;
import io.swagger.annotations.Authorization;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.Servlet;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController
{
    @Autowired
    UserServices userServices;

    @GetMapping(value = "/users")
    public ResponseEntity<?> listAllUsers()
    {
        List<User> myList = userServices.listAll();
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = "/myinfo")
    public ResponseEntity<?> getUserInfo( Authentication authentication)
    {
        User u = userServices.findByUsername(authentication.getName());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @PutMapping(value = "/user/{id}", consumes = "application/json")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserMinimum user, @PathVariable long id)
    {
        User newUser = userServices.update(user, id);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id)
    {
        userServices.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
