package io.hexlet.spring.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
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


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    private Post post;

    private User user;

    @BeforeEach
    public void beforeEach() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getAuthor))
                .create();
        post.setAuthor(user);
        user.getPosts().add(post);
        userRepository.save(user);
    }

    @Test
    public void testIndex() throws Exception {
        postRepository.save(post);
        mockMvc.perform(get("/api/posts")
                        .param("page", "1")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        postRepository.save(post);
        var response = mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(post.getTitle()),
                v -> v.node("content").isEqualTo(post.getContent()),
                v -> v.node("published").isEqualTo(post.getPublished()),
                v -> v.node("authorId").isEqualTo(post.getAuthor().getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = postMapper.map(post);
        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var response = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        assertThatJson(body).isObject();
    }

    @Test
    public void testUpdate() throws Exception {
        postRepository.save(post);
        var dto = new PostUpdateDTO();
        dto.setTitle("I love you");
        dto.setContent("Hahahahaha");

        var request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();
        assertThatJson(body).isObject();
        post = postRepository.findById(post.getId()).get();
        assertThat(post.getTitle()).isEqualTo(dto.getTitle());
        assertThat(post.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    public void testPatch() throws Exception {
        postRepository.save(post);
        var dto = new PostPatchDTO();
        dto.setTitle(JsonNullable.of("I love you"));

        var request = patch("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();
        assertThatJson(body).isObject();
        post = postRepository.findById(post.getId()).get();
        assertThat(post.getTitle()).isEqualTo(dto.getTitle().get());
    }

    @Test
    public void testDestroy() throws Exception {
        postRepository.save(post);
        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        post = postRepository.findById(post.getId()).orElse(null);
        assertThat(post).isNull();
    }
}
