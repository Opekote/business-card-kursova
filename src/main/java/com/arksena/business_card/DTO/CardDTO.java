package com.arksena.business_card.DTO;

import com.arksena.business_card.enums.CardType;
import lombok.Data;

@Data
public class CardDTO {
    private String title;
    private String description;
    private String contactInfo;
    private CardType cardType;
    private String data;
}
