package ru.screener.user.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.screener.user.dto.user.UserResponse;
import ru.screener.user.entity.user.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    @Mapping(source = "country.locale", target = "locale")
    UserResponse toUserResponse(User user);
}
