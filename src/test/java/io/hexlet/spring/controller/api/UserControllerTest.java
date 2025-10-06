package io.hexlet.spring.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserPatchDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.mapper.UserMapper;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
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
    private UserMapper userMapper;

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
                .ignore(Select.field(User::getPosts)).ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(user);
        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(user);
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
        var dto = userMapper.map(user);
        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
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
        userRepository.save(user);
        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        user = userRepository.findById(user.getId()).orElse(null);
        assertThat(user).isNull();
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(user);
        var dto = new UserUpdateDTO();
        dto.setFirstName("Ethan");
        dto.setLastName("Hunt");
        dto.setEmail("adfg@gmail.com");

        var request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).isObject();
        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    public void testPatch() throws Exception {
        userRepository.save(user);
        var dto = new UserPatchDTO();
        dto.setFirstName(JsonNullable.of("Ethan"));
        var request = patch("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName().get());
    }
}
