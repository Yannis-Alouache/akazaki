package com.akazaki.api.infrastructure.web.controller.user;

import com.akazaki.api.application.queries.GetAllUsersQueryHandler;
import com.akazaki.api.application.queries.GetUserQueryHandler;
import com.akazaki.api.domain.ports.in.queries.GetAllUsersQuery;
import com.akazaki.api.domain.ports.in.queries.GetUserQuery;
import com.akazaki.api.infrastructure.web.dto.user.response.UserResponse;
import com.akazaki.api.infrastructure.web.mapper.user.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
public class UserController {
    private final GetAllUsersQueryHandler getAllUsersQueryHandler;
    private final GetUserQueryHandler getUserQueryHandler;
    private final UserMapper userMapper;

    public UserController(
            GetAllUsersQueryHandler getAllUsersQueryHandler,
            GetUserQueryHandler getUserQueryHandler,
            UserMapper userMapper) {
        this.getAllUsersQueryHandler = getAllUsersQueryHandler;
        this.getUserQueryHandler = getUserQueryHandler;
        this.userMapper = userMapper;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var users = getAllUsersQueryHandler.handle(new GetAllUsersQuery());
        var response = userMapper.toResponseList(users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        var user = getUserQueryHandler.handle(new GetUserQuery(id));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }
} 