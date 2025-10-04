package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserPatchDTO {
    @NotBlank
    private JsonNullable<String> firstName = JsonNullable.undefined();
    @NotBlank
    private JsonNullable<String> lastName = JsonNullable.undefined();
    @NotBlank
    private JsonNullable<String> email = JsonNullable.undefined();
}
