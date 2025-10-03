package io.hexlet.spring.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
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
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper om;

    private Post post;

    @BeforeEach
    public void beforeEach() {
        postRepository.deleteAll();
        post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .create();
        postRepository.save(post);
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("page", "1")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(post.getTitle()),
                v -> v.node("content").isEqualTo(post.getContent()),
                v -> v.node("published").isEqualTo(post.getPublished())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(PostCreateDTO.class)
                .create();
        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
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
        var data = new HashMap<String, String>();
        data.put("title", "I love you");
        data.put("content", "Hahahahaha");

        var request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = result.getContentAsString();
        assertThatJson(body).isObject();
        post = postRepository.findById(post.getId()).get();
        assertThat(post.getTitle()).isEqualTo("I love you");
        assertThat(post.getContent()).isEqualTo(data.get("content"));
    }

    @Test
    public void testDestroy() throws Exception {
        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        post = postRepository.findById(post.getId()).orElse(null);
        assertThat(post).isNull();
    }
}
