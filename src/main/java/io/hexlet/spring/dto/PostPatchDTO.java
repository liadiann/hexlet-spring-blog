package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostPatchDTO {
    @NotBlank
    @Size(min = 1, max = 15)
    private JsonNullable<String> title = JsonNullable.undefined();
    @NotBlank
    private JsonNullable<String> content = JsonNullable.undefined();
}
