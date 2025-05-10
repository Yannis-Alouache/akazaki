package com.akazaki.api.e2e;


import com.akazaki.api.config.JwtFactory;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Category.CategoryRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.User.UserRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@DisplayName("Create Product E2E Tests")
public class CreateProductE2ETest {
    private final MockMvc mockMvc;
    private final JwtFactory jwtFactory;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private String jwtToken;
    private Category category;

    @Autowired
    public CreateProductE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, UserRepositoryAdapter userRepository, CategoryRepositoryAdapter categoryRepository) {
        this.mockMvc = mockMvc;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", true);
        User persistedUser = userRepository.save(domainUser);

        Category domainCategory = new Category(null, "Drink");
        this.category = categoryRepository.save(domainCategory);
        
        this.jwtToken = jwtFactory.getJwtToken(persistedUser);
    }

    @Test
    @DisplayName("Create A Product Successfully")
    public void createProductSuccessfully() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                new FileSystemResource("src/test/resources/images/test-image.png").getContentAsByteArray()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/products")
                    .file(image)
                    .param("name", "Product Name")
                    .param("description", "Product Description")
                    .param("price", "100")
                    .param("stock", "10")
                    .param("categoryIds", this.category.getId().toString())
                    .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}