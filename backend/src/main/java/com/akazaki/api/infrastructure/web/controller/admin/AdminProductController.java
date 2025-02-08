package com.akazaki.api.infrastructure.web.controller.admin;
// package com.akazaki.api.infrastructure.web.controller.admin.product;

// import com.akazaki.api.domain.model.Category;
// import com.akazaki.api.domain.model.Product;
// import com.akazaki.api.domain.ports.in.queries.GetAllUsersQuery;
// import com.akazaki.api.infrastructure.web.controller.auth.AuthController;
// import com.akazaki.api.infrastructure.web.dto.auth.request.RegisterUserRequest;
// import com.akazaki.api.infrastructure.web.dto.auth.response.RegisterUserResponse;
// import com.akazaki.api.infrastructure.web.dto.request.CreateProductRequest;
// import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
// import com.akazaki.api.infrastructure.web.dto.response.UserResponse;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/admin/products")
// @Tag(name = "Product", description = "Product administration routes")
// public class AdminProductController {

//     private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);

//     public AdminProductController() {

//     }

//     @Operation(
//         summary = "Create a new product",
//         description = "Creates a new product with the provided information including an image file"
//     )
//     @ApiResponses({
//         @ApiResponse(
//             responseCode = "200",
//             description = "Product successfully created",
//             content = @Content(schema = @Schema(implementation = ProductResponse.class))
//         ),
//         @ApiResponse(
//             responseCode = "400",
//             description = "Invalid input",
//             content = @Content
//         )
//     })
//     @PostMapping(value = "")
//     public ResponseEntity<ProductResponse> createProduct(@Valid @ModelAttribute CreateProductRequest request) {
//         logger.info("Received create product request for: {}", request.toString());
        
//         return null;
//     }
// }