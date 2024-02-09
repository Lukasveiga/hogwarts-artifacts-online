package com.devlukas.hogwartsartifactsonline.hogwartsuser.converter;

import com.devlukas.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.devlukas.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        var user = new HogwartsUser();
        user.setId(source.id());
        user.setUsername(source.username());
        user.setEnable(source.enabled());
        user.setRoles(source.roles());
        return user;
    }
}
