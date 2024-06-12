package org.example.roomrelish.mapper;

import org.example.roomrelish.dto.RegisterRequest;
import org.example.roomrelish.dto.RegisterUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface RegisterUserMapper {
    RegisterUserMapper INSTANCE = Mappers.getMapper(RegisterUserMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(target = "address", expression = "java(mapAddress(registerUserDTO.getStreet(),registerUserDTO.getCity(),registerUserDTO.getState()))")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", qualifiedByName = "dateOfBirth")
    RegisterRequest registerUserDTOToRegisterRequest(RegisterUserDTO registerUserDTO);

    @Named("mapAddress")
    default String mapAddress(String street, String city, String state) {
        return street + ", " + city + ", " + state;
    }

    @Named("dateOfBirth")
    default Date mapDateOfBirth(String dateOfBirth) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.parse(dateOfBirth);
    }
}
