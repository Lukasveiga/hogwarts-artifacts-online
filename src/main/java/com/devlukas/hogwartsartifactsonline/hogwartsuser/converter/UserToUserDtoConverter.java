package com.devlukas.hogwartsartifactsonline.hogwartsuser.converter;

import com.devlukas.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.devlukas.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        return new UserDto(
                source.getId(),
                source.getUsername(),
                source.isEnable(),
                source.getRoles()
        );
    }
}
