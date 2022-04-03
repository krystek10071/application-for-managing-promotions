package com.example.managingpromotions.services;

import com.example.managingpromotions.exception.UserNotFoundException;
import com.example.managingpromotions.mapper.GroceryListMapper;
import com.example.managingpromotions.models.GroceryList;
import com.example.managingpromotions.models.UserApp;
import com.example.managingpromotions.models.repository.GroceryListRepository;
import com.example.managingpromotions.models.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.managingPromotions.api.model.CreateIdResponse;
import pl.managingPromotions.api.model.GroceryListRequestDTO;
import pl.managingPromotions.api.model.GroceryListResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class GroceryListService {

    private final GroceryListRepository groceryListRepository;
    private final GroceryListMapper groceryListMapper;
    private final UserRepository userRepository;

    @Transactional()
    public CreateIdResponse createGroceryList(GroceryListRequestDTO groceryListRequestDTO) {
        Optional<UserApp> user = userRepository.findByUsername(groceryListRequestDTO.getUserLogin());

        GroceryList groceryList = groceryListMapper.mapGroceryListRequestDTOToGroceryList(groceryListRequestDTO);
        groceryList.setUserApp(user.orElseThrow(() -> new UserNotFoundException(groceryListRequestDTO.getUserLogin())));
        groceryList.setCreateDate(LocalDate.now());

        GroceryList savedGroceryList = groceryListRepository.save(groceryList);

        return CreateIdResponse.builder()
                .id(savedGroceryList.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<GroceryListResponseDTO> getGroceryListForUser(String userName) {
        Long userId = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(userName)).getId();
        List<GroceryList> groceryLists = groceryListRepository.findByUserAppId(userId);

        return groceryListMapper.mapGroceryListToGroceryListResponseDTO(groceryLists);
    }
}
