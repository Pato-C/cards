package com.logicea.cards.service;

import com.logicea.cards.component.JwtTokenProvider;
import com.logicea.cards.dto.CardRequest;
import com.logicea.cards.dto.CardResponse;
import com.logicea.cards.dto.CardSearchCriteria;
import com.logicea.cards.entity.CardsEntity;
import com.logicea.cards.entity.UserEntity;
import com.logicea.cards.models.CardStatus;
import com.logicea.cards.repository.CardRepository;
import jakarta.persistence.criteria.Predicate;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public static Logger logger = LogManager.getLogger("com.logicea.cards");
    public CardsEntity createCard(CardRequest cardRequestDto, String token) {
        try {
            logger.info("Going to create card with name " + cardRequestDto.getName());

            UserEntity userEntity = jwtTokenProvider.getUserFromToken(token);
            CardsEntity card = new CardsEntity();
            card.setUser(userEntity);
            card.setName(cardRequestDto.getName());
            card.setDescription(cardRequestDto.getDescription());
            String color = cardRequestDto.getColor();
            card.setColor(color);
            card.setStatus(CardStatus.TO_DO);
            card.setCreatedOn(LocalDateTime.now());
            card.setUpdatedOn(LocalDateTime.now());
            return cardRepository.save(card);
        } catch (Exception e) {
            logger.error("Error occurred while creating card: " + e.getMessage());
            throw new RuntimeException("Error occurred while creating card. Please check server logs!!");
        }
    }

    public Page<CardResponse> searchCards(CardSearchCriteria cardSearchCriteria, Pageable pageable, boolean isAdmin, Long currentUserId) {
        try {
            Specification<CardsEntity> specification = createSpecification(cardSearchCriteria, isAdmin, currentUserId);
            Page<CardsEntity> cards = cardRepository.findAll(specification, pageable);
            List<CardResponse> response = new ArrayList<>();
            for (CardsEntity card : cards) {
                CardResponse dto = new CardResponse();
                dto.setId(card.getId());
                dto.setUserId(card.getUser().getId());
                dto.setName(card.getName());
                dto.setDescription(card.getDescription());
                dto.setColor(card.getColor());
                dto.setStatus(card.getStatus());
                dto.setCreatedOn(card.getCreatedOn());
                dto.setUpdatedOn(card.getUpdatedOn());
                response.add(dto);
            }
            return new PageImpl<>(response, pageable, cards.getTotalElements());
        } catch (Exception e) {
           logger.error("Error fetching cards "+e.getMessage());
            throw new RuntimeException("Error occurred while searching cards");
        }
    }

    private Specification<CardsEntity> createSpecification(CardSearchCriteria searchCriteria, boolean isAdmin, Long currentUserId) {
        try {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchCriteria.getName() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), searchCriteria.getName()));
                }

                if (searchCriteria.getColor() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("color"), searchCriteria.getColor()));
                }

                if (searchCriteria.getStatus() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), searchCriteria.getStatus()));
                }

                if (searchCriteria.getCreationDate() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("createdOn"), searchCriteria.getCreationDate()));
                }

                if (!isAdmin) {
                    predicates.add(criteriaBuilder.equal(root.get("user").get("id"), currentUserId));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        } catch (Exception e) {
            logger.error("Error occurred while creating specification: " + e.getMessage());
            throw new RuntimeException("Error occurred while creating specification!!");
        }
    }
    public Optional<CardsEntity> getCardByNameAndUser(String cardName, UserEntity user) {
        try {
            Optional<CardsEntity> card = cardRepository.findByNameAndUser(cardName, user);
            return card;
            }
        catch (Exception e)
        {
            logger.error("Error occurred while creating specification: " + e.getMessage());
            throw new RuntimeException("Error fetching card!!");
        }

    }
    public CardsEntity getCardById(Long cardId)  {
        return cardRepository.findById(cardId).orElse(null);
    }
    public CardsEntity updateCard(CardsEntity card) {
        return cardRepository.save(card);
    }


    public void deleteCard(Long cardId) {
        CardsEntity card = cardRepository.findById(cardId).orElse(null);
        assert card != null;
        cardRepository.delete(card);
    }
    public CardResponse convertToResponse(Optional<CardsEntity> cardEntity) {
        CardResponse cardResponse = new CardResponse();
        cardResponse.setId(cardEntity.get().getId());
        cardResponse.setUserId(cardEntity.get().getUser().getId());
        cardResponse.setName(cardEntity.get().getName());
        cardResponse.setDescription(cardEntity.get().getDescription());
        cardResponse.setColor(cardEntity.get().getColor());
        cardResponse.setStatus(cardEntity.get().getStatus());
        cardResponse.setCreatedOn(cardEntity.get().getCreatedOn());
        cardResponse.setUpdatedOn(cardEntity.get().getUpdatedOn());
        return cardResponse;
    }






}
