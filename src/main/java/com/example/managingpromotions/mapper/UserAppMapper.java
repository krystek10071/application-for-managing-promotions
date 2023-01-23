package com.example.managingpromotions.mapper;

import com.example.managingpromotions.model.UserApp;
import org.mapstruct.Mapper;
import pl.managingPromotions.api.model.UserAppResponseDTO;

@Mapper(componentModel = "spring")
public interface UserAppMapper {

    UserAppResponseDTO mapUserAppToUserAppMapper(UserApp userApp);
}
