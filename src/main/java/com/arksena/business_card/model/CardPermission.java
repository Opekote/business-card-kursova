package com.arksena.business_card.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "card_permissions")
public class CardPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
