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
import pl.managingPromotions.api.model.GroceryListProductDTO;
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

        final GroceryList groceryList = groceryListMapper.mapGroceryListRequestDTOToGroceryList(groceryListRequestDTO);

        groceryList.setUserApp(user.orElseThrow(() -> new UserNotFoundException(groceryListRequestDTO.getUserLogin())));
        groceryList.setCreateDate(LocalDate.now());

        assignGroceryElementToGroceryList(groceryList);

        final GroceryList savedGroceryList = groceryListRepository.saveAndFlush(groceryList);

        return CreateIdResponse.builder()
                .id(savedGroceryList.getId())
                .build();
    }

    @Transactional
    public void patchGroceryList (final long groceryListId, List<GroceryListProductDTO> groceryListProductDTOS){

        final Optional<GroceryList> groceryList = groceryListRepository.findById(groceryListId);

        //todo implements exception
        final GroceryList savedGroceryList = groceryList.orElseThrow();

        savedGroceryList.setGroceryElements(groceryListMapper.mapListGroceryListProductDTOToListGroceryElement(groceryListProductDTOS));
        savedGroceryList.setModifyDate(LocalDate.now());

        groceryListRepository.save(savedGroceryList);
    }

    @Transactional(readOnly = true)
    public List<GroceryListResponseDTO> getGroceryListForUser(String userName) {
        Long userId = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(userName)).getId();
        List<GroceryList> groceryLists = groceryListRepository.findByUserAppId(userId);

        return groceryListMapper.mapGroceryListToGroceryListResponseDTO(groceryLists);
    }

    private void assignGroceryElementToGroceryList(GroceryList groceryList) {
        groceryList.getGroceryElements().forEach(groceryElement -> {
            groceryElement.setGroceryList(groceryList);
        });
    }
}
