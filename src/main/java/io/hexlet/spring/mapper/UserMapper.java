package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserPatchDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(passwordEncoder.encode(password));
    }

    public abstract UserDTO map(User user);
    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO dto);
    public abstract void update(@Valid UserUpdateDTO dto, @MappingTarget User user);
    public abstract void update(@Valid UserPatchDTO dto, @MappingTarget User user);
}
