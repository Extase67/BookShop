package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
import org.junit.jupiter.api.DisplayName;
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
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
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
                    new ClassPathResource("database/books/insert-books.sql")
            );
        }
    }

    @BeforeAll
    static void beforeAll(@Autowired CategoryRepository categoryRepository) {
        categoryRepository.save(new Category("Fantasy"));
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
                    new ClassPathResource("database/books/cleanup-db.sql")
            );
        }
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        // given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setAuthor("Joseph Heller")
                .setPrice(BigDecimal.valueOf(15.99))
                .setTitle("Catch-22")
                .setIsbn("12324564789")
                .setDescription("Great novel.")
                .setCategories(List.of());

        BookDto expected = new BookDto()
                .setId(1L)
                .setAuthor(createBookRequestDto.getAuthor())
                .setPrice(createBookRequestDto.getPrice())
                .setTitle(createBookRequestDto.getTitle())
                .setIsbn(createBookRequestDto.getIsbn())
                .setDescription(createBookRequestDto.getDescription())
                .setCategories(createBookRequestDto.getCategories());

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        Category category = new Category("Fantasy");
        category.setId(1L);

        // when
        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();
        // then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @DisplayName("Get all books")
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getAll_GivenBooksInCatalog_ShouldReturnAllProducts() throws Exception {
        // given
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L)
                .setAuthor("J.K.Rowling")
                .setTitle("Harry Potter")
                .setPrice(BigDecimal.valueOf(45.99))
                .setDescription("Magic book.")
                .setIsbn("123456789")
                .setCategories(List.of()));

        expected.add(new BookDto().setId(2L)
                .setAuthor("Franz Kafka")
                .setTitle("Castle")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Great novel.")
                .setIsbn("223456789")
                .setCategories(List.of()));

        expected.add(new BookDto().setId(3L)
                .setAuthor("Franz Kafka")
                .setTitle("The Trial")
                .setPrice(BigDecimal.valueOf(7.99))
                .setDescription("Great novel.")
                .setIsbn("323456789")
                .setCategories(List.of()));

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/books")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // then
        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i]),
                    "Objects at index " + i + " are not equal");
        }
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @DisplayName("Get all books")
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void getAll_GivenEmptyCatalog_ShouldReturnEmptyList() throws Exception {
        // given
        List<BookDto> expected = new ArrayList<>();
        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/books")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // then
        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void getBookById_GivenBooksInCatalog_ShouldReturnBook() throws Exception {
        // given
        BookDtoWithoutCategoryIds expected = new BookDtoWithoutCategoryIds();
        expected.setId(1L);
        expected.setAuthor("JJ.K.Rowling");
        expected.setTitle("Harry Potter");
        expected.setPrice(BigDecimal.valueOf(45.99));
        expected.setIsbn("1232456789");
        expected.setDescription("Magic book.");

        // when
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/books/1")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        BookDtoWithoutCategoryIds.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void getBookById_NegativeId_ShouldReturnStatusNotFound() throws Exception {
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/books/-1")
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void update_CorrectCreateBookRequestDto_Success() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Castle")
                .setAuthor("Franz Kafka")
                .setPrice(BigDecimal.valueOf(40))
                .setIsbn("123456333")
                .setDescription("Great Novel.")
                .setCategories(List.of());

        BookDto expected = new BookDto()
                .setId(2L)
                .setPrice(createBookRequestDto.getPrice())
                .setTitle(createBookRequestDto.getTitle())
                .setAuthor(createBookRequestDto.getAuthor())
                .setDescription(createBookRequestDto.getDescription())
                .setIsbn(createBookRequestDto.getIsbn())
                .setCategories(createBookRequestDto.getCategories());

        String jsonRequest = objectMapper.writeValueAsString(expected);

        // when
        MvcResult result = mockMvc.perform(put("/api/books/2")
                        .content(jsonRequest)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        // then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void createBook_InvalidTitle_ShouldReturnBadRequest() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("");
        requestDto.setAuthor("Author");
        requestDto.setPrice(BigDecimal.valueOf(5));

        mockMvc.perform(post("/api/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_ExistingId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
