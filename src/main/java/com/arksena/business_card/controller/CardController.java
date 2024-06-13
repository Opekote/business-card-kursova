package com.arksena.business_card.controller;

import com.arksena.business_card.DTO.CardDTO;
import com.arksena.business_card.model.Card;
import com.arksena.business_card.repository.CardRepository;
import com.arksena.business_card.service.CardService;
import com.arksena.business_card.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cards", description = "Operations related to Cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CardDTO> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get card by an id", description = "Returns a single card. If user have a permission to see card it will return card and 200 (OK). ")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = cardService.getUserIdByUsername(userDetails.getUsername());
        if (cardService.hasAccess(id, currentUserId)) {
            Optional<CardDTO> cardDTO = cardService.getCardDTOById(id);
            return cardDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Creating a new card", description = "Method for creating cards ")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = cardService.getUserIdByUsername(userDetails.getUsername());
        Card card = new Card();
        card.setUser(userService.getUserById(currentUserId));
        card.setTitle(cardDTO.getTitle());
        card.setDescription(cardDTO.getDescription());
        card.setContactInfo(cardDTO.getContactInfo());
        card.setCardType(cardDTO.getCardType());
        card.setData(cardDTO.getData());
        Card savedCard = cardService.saveCard(card);
        CardDTO responseCardDTO = cardService.convertToDTO(savedCard);
        return ResponseEntity.ok(responseCardDTO);
    }

    @DeleteMapping("/{cardID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Delete card", description = "Method for deleting cards. ")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardID, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = cardService.getUserIdByUsername(userDetails.getUsername());
        if (cardService.hasAccess(cardID, currentUserId)) {
            cardService.deleteCard(cardID);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PatchMapping("/{cardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Update card", description = "Method for updating single cards. ")

    public ResponseEntity<String> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardDTO request
    ) {
        Card existingCard = cardService.getCardById(cardId);
        if (existingCard == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getTitle() != null) {
            existingCard.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingCard.setDescription(request.getDescription());
        }
        if (request.getContactInfo() != null) {
            existingCard.setContactInfo(request.getContactInfo());
        }
        if (request.getCardType() != null) {
            existingCard.setCardType(request.getCardType());
        }
        if (request.getData() != null) {
            existingCard.setData(request.getData());
        }

        cardRepository.save(existingCard);

        return ResponseEntity.ok("Card updated successfully");
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Share card", description = "Method for sharing cards with other users ")
    public ResponseEntity<Void> shareCard(@PathVariable Long id, @RequestParam Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = cardService.getUserIdByUsername(userDetails.getUsername());
        if (cardService.hasAccess(id, currentUserId)) {
            cardService.addPermission(id, userId);
        }
        return ResponseEntity.ok().build();
    }
}