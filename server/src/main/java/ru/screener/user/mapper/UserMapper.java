package ru.screener.user.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.screener.user.entity.AppUser;
import ru.screener.user.entity.UserSettings;
import ru.screener.user.model.domain.ProfileDto;
import ru.screener.user.model.domain.SettingsDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="id", source="id")
    ProfileDto toProfile(AppUser user);

    SettingsDto toSettings(UserSettings userSettings);
}
