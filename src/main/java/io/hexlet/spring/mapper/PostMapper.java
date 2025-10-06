package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import org.mapstruct.*;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostMapper {
    @Mapping(target = "authorId", source = "author.id")
    public abstract PostDTO map(Post post);
    @Mapping(target = "author.id", source = "authorId")
    public abstract Post map(PostCreateDTO dto);
    public abstract void update(PostUpdateDTO dto, @MappingTarget Post post);
    public abstract void update(PostPatchDTO dto, @MappingTarget Post post);
}
