package com.example.Enotes_API_Service.integration;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.entity.Category;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class CategoryControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    CategoryDto categoryDto = null;
    Category category = null;

    @BeforeEach
    public void initialize() {
        categoryDto = CategoryDto.builder()
                .id(null)
                .name("Java Notes")
                .description("Java Notes")
                .isActive(true)
                .build();

        category = Category.builder()
                .id(null)
                .name("Java Notes")
                .description("Java Notes")
                .isActive(true)
                .isDeleted(false).build();
    }

    @Test
    public void testSaveCategory() throws Exception {

        String token = generateToken("nymansa.gupta@gmail.com", "qwertyui");

        mockMvc.perform(post("/api/v1/category/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("saved success"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    public String generateToken(String email, String password) throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword(password);

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        String token = root.path("data").path("token").asText();
        return "Bearer "+token;
    }
}

