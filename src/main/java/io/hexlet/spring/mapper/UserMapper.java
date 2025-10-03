package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import jakarta.validation.Valid;
import org.mapstruct.*;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract UserDTO map(User user);
    public abstract User map(UserCreateDTO dto);
    public abstract void update(@Valid UserUpdateDTO dto, @MappingTarget User user);
}
