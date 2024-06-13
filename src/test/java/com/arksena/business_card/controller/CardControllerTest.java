package com.arksena.business_card.controller;

import com.arksena.business_card.DTO.CardDTO;
import com.arksena.business_card.service.CardService;
import com.arksena.business_card.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @Mock
    private UserService  userService;

    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new User("username", "password", Collections.emptyList());
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCards() {
        when(cardService.getAllCards()).thenReturn(Collections.singletonList(new CardDTO()));

        assertEquals(1, cardController.getAllCards().size());
        verify(cardService, times(1)).getAllCards();
    }

    @Test
    public void testGetCardById_Success() {
        Long cardId = 1L;
        UserDetails userDetails = new User("username", "password", Collections.emptyList());
        when(cardService.getUserIdByUsername(userDetails.getUsername())).thenReturn(1L);
        when(cardService.hasAccess(cardId, 1L)).thenReturn(true);
        CardDTO cardDTO = new CardDTO();
        when(cardService.getCardDTOById(cardId)).thenReturn(Optional.of(cardDTO));

        ResponseEntity<CardDTO> responseEntity = cardController.getCardById(cardId, userDetails);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cardDTO, responseEntity.getBody());
    }

    @Test
    public void testGetCardById_Forbidden() {
        Long cardId = 1L;
        UserDetails userDetails = new User("username", "password", Collections.emptyList());
        when(cardService.getUserIdByUsername(userDetails.getUsername())).thenReturn(1L);
        when(cardService.hasAccess(cardId, 1L)).thenReturn(false);

        ResponseEntity<CardDTO> responseEntity = cardController.getCardById(cardId, userDetails);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }



    @Test
    public void testDeleteCard() {
        UserDetails userDetails = new User("username", "password", Collections.emptyList());
        Long currentUserId = 1L;
        when(cardService.getUserIdByUsername(userDetails.getUsername())).thenReturn(currentUserId);

        Long cardId = 1L;

        when(cardService.hasAccess(cardId, currentUserId)).thenReturn(true);

        ResponseEntity<Void> responseEntity = cardController.deleteCard(cardId, userDetails);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(cardService, times(1)).deleteCard(cardId);
    }

    @Test
    public void testShareCard() {
        UserDetails userDetails = new User("username", "password", Collections.emptyList());
        Long currentUserId = 1L;
        when(cardService.getUserIdByUsername(userDetails.getUsername())).thenReturn(currentUserId);

        Long cardId = 1L;
        Long userId = 2L;

        when(cardService.hasAccess(cardId, currentUserId)).thenReturn(true);

        ResponseEntity<Void> responseEntity = cardController.shareCard(cardId, userId, userDetails);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(cardService, times(1)).addPermission(cardId, userId);
    }

}
