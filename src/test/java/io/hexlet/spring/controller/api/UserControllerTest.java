package io.hexlet.spring.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    private User user;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(user.getFirstName()),
                v -> v.node("lastName").isEqualTo(user.getLastName()),
                v -> v.node("email").isEqualTo(user.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(UserCreateDTO.class)
                .supply(Select.field(UserCreateDTO::getEmail), () -> faker.internet().emailAddress())
                .create();
        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();
        assertThatJson(body).isObject();
    }

    @Test
    public void testDestroy() throws Exception {
        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        user = userRepository.findById(user.getId()).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("firstName", "Ethan");
        data.put("lastName", "Hunt");

        var request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        var response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).isObject();
        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo("Ethan");
        assertThat(user.getLastName()).isEqualTo("Hunt");
    }
}
