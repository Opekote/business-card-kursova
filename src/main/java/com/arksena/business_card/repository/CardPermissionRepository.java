package com.arksena.business_card.repository;

import com.arksena.business_card.model.CardPermission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPermissionRepository extends JpaRepository<CardPermission, Long> {

    @Transactional
    void deleteByCardId(Long cardId);
}