package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.category.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/books/category/add-three-default-categories.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/category/remove-all-categories.sql")
            );
        }
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @Test
    @Sql(scripts = "classpath:database/books/category/delete-category-science.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        // given
        CategoryDto requestDto = new CategoryDto()
                .setName("Science")
                .setDescription("Science books");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // when
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertNotNull(actual.getId());
        assertEquals(requestDto.getName(), actual.getName());
        assertEquals(requestDto.getDescription(), actual.getDescription());
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @Sql(scripts = "classpath:database/books/category/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/category/add-three-default-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        // given
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto().setId(1L)
                .setName("Horror")
                .setDescription("Scary books"));
        expected.add(new CategoryDto().setId(2L)
                .setName("For kids")
                .setDescription("Books for kids"));
        expected.add(new CategoryDto().setId(3L)
                .setName("History")
                .setDescription("History books"));

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // then
        CategoryDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void getCategoryById_GivenCategoriesInCatalog_ShouldReturnCategory() throws Exception {
        // given
        CategoryDto expected = new CategoryDto();
        expected.setId(2L)
                .setName("For kids")
                .setDescription("Books for kids");

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/2")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        CategoryDto.class);

        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void getCategoryById_NegativeId_ShouldReturnStatusNotFound() throws Exception {
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/-1")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void updateCategory_CorrectCategoryDto_Success() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setId(2L)
                .setName("For kids")
                .setDescription("Books not only for kids");
        String jsonRequest = objectMapper.writeValueAsString(expected);
        // when
        MvcResult result = mockMvc.perform(put("/categories/2")
                        .content(jsonRequest)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void createCategoryInvalidName_ShouldReturnBadRequest() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("");
        categoryDto.setDescription("Author");

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @Sql(scripts = "classpath:database/books/category/remove-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/category/add-three-default-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ExistingId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }
}
