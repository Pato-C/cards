package com.logicea.cards.repository;

import com.logicea.cards.entity.CardsEntity;
import com.logicea.cards.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardsEntity, Long> {
    Page<CardsEntity> findAll(Specification<CardsEntity> specification, Pageable pageable);
    Optional<CardsEntity> findByNameAndUser(String cardName, UserEntity user);
    Optional<CardsEntity> findByName(String cardName);
}
