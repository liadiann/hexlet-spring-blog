package io.hexlet.spring.specification;

import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.model.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostSpecification {
    public Specification<Post> build(PostParamsDTO params) {
        return withAuthorId(params.getAuthorId())
                .and(withCreatedAtGt(params.getCreatedAtGt()))
                .and(withCreatedAtLt(params.getCreatedAtLt()))
                .and(withPublishedTrue(params.getPublishedTrue()));
    }

    private Specification<Post> withAuthorId(Long authorId) {
        return (root, query, cb) ->
                authorId == null ? cb.conjunction() : cb.equal(root.get("author").get("id"), authorId);
    }

    private Specification<Post> withCreatedAtGt(LocalDateTime date) {
        return (root, query, cb) ->
                date == null ? cb.conjunction() : cb.greaterThan(root.get("createdAt"), date);
    }

    private Specification<Post> withCreatedAtLt(LocalDateTime date) {
        return (root, query, cb) ->
                date == null ? cb.conjunction() : cb.lessThan(root.get("createdAt"), date);
    }

    private Specification<Post> withPublishedTrue(Boolean published) {
        return (root, query, cb) ->
                published == null ? cb.conjunction() : cb.equal(root.get("published"), true);
    }
}
