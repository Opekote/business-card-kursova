package com.arksena.business_card.service;

import com.arksena.business_card.DTO.CardDTO;
import com.arksena.business_card.DTO.UserDTO;
import com.arksena.business_card.enums.Role;
import com.arksena.business_card.model.Card;
import com.arksena.business_card.model.CardPermission;
import com.arksena.business_card.model.User;
import com.arksena.business_card.repository.CardPermissionRepository;
import com.arksena.business_card.repository.CardRepository;
import com.arksena.business_card.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardPermissionRepository cardPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CardDTO> getAllCards() {
        return cardRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(Long cardID) {
        cardPermissionRepository.deleteByCardId(cardID);
        cardRepository.deleteById(cardID);
    }
    public boolean hasAccess(Long cardId, Long userId) {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isPresent()) {
            User user = userRepository.findById(userId).orElseThrow();
            return card.get().getUser().getId().equals(userId) || user.getRole() == Role.ADMIN;
        }
        return false;
    }

    public void addPermission(Long cardId, Long userId) {
        CardPermission permission = new CardPermission();
        if(cardRepository.findById(cardId).isPresent() && userRepository.findById(userId).isPresent())
        {
            permission.setCard(cardRepository.findById(cardId).orElseThrow());
            permission.setUser(userRepository.findById(userId).orElseThrow());
        }

        cardPermissionRepository.save(permission);
    }

    public boolean isOwner(Long cardId, Long userId) {
        Optional<Card> card = cardRepository.findById(cardId);
        return card.isPresent() && card.get().getUser().getId().equals(userId);
    }


    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


    public Optional<CardDTO> getCardDTOById(Long id) {
        return cardRepository.findById(id).map(this::convertToDTO);
    }

    public Card getCardById(long id){
        return cardRepository.findById(id).orElseThrow();
    }

    public CardDTO convertToDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setTitle(card.getTitle());
        cardDTO.setDescription(card.getDescription());
        cardDTO.setContactInfo(card.getContactInfo());
        cardDTO.setCardType(card.getCardType());
        cardDTO.setData(card.getData());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(card.getUser().getId());
        userDTO.setUsername(card.getUser().getUsername());
        return cardDTO;
    }
}
