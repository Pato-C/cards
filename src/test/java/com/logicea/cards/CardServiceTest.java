package com.logicea.cards;

import com.logicea.cards.component.JwtTokenProvider;
import com.logicea.cards.dto.CardRequest;
import com.logicea.cards.dto.CardResponse;
import com.logicea.cards.dto.CardSearchCriteria;
import com.logicea.cards.entity.CardsEntity;
import com.logicea.cards.entity.UserEntity;
import com.logicea.cards.models.CardStatus;
import com.logicea.cards.repository.CardRepository;
import com.logicea.cards.service.CardService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private CardService cardService;

    @Test
    public void createCard_ValidRequest_ReturnsCreatedCard() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("Test Card");
        cardRequest.setDescription("This is a test card");
        cardRequest.setColor("#FFFFFF");

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNAZ21haWwuY29tIiwicm9sZSI6Ik1lbWJlciIsImlhdCI6MTY4NjkwMjkwNCwiZXhwIjoxNjg2OTg5MzA0fQ.VvHMQ9S7t5EoxGCTEL5zY_bs0gXd3qE9fUR5SlIrrEg";

        UserEntity user = new UserEntity();
        user.setId(1L);

        CardsEntity savedCard = new CardsEntity();
        savedCard.setId(1L);
        savedCard.setName("Test Card");
        savedCard.setDescription("This is a test card");
        savedCard.setColor("#FFFFFF");
        savedCard.setStatus(CardStatus.TO_DO);
        savedCard.setCreatedOn(LocalDateTime.now());
        savedCard.setUpdatedOn(LocalDateTime.now());
        savedCard.setUser(user);
        Mockito.when(jwtTokenProvider.getUserFromToken(Mockito.anyString())).thenReturn(user);
        Mockito.when(cardRepository.save(Mockito.any(CardsEntity.class))).thenReturn(savedCard);
        CardsEntity createdCard = cardService.createCard(cardRequest, token);
        Assert.assertNotNull(createdCard);
        Assert.assertEquals(1L, createdCard.getId().longValue());
        Assert.assertEquals("Test Card", createdCard.getName());

    }



    @Test
    public void searchCards_ValidRequest_ReturnsCardPage() {
        CardSearchCriteria searchCriteria = new CardSearchCriteria();
        searchCriteria.setName("Test Card");
        searchCriteria.setColor("#FFFFFF");
        searchCriteria.setStatus(CardStatus.TO_DO);
        searchCriteria.setCreationDate(LocalDate.now());
        UserEntity user = new UserEntity();
        user.setId(1L);
        CardsEntity card1 = new CardsEntity();
        card1.setId(1L);
        card1.setName("Test Card");
        card1.setDescription("This is a test card");
        card1.setColor("#FFFFFF");
        card1.setStatus(CardStatus.TO_DO);
        card1.setCreatedOn(LocalDateTime.now());
        card1.setUpdatedOn(LocalDateTime.now());
        card1.setUser(user);
        CardsEntity card2 = new CardsEntity();
        card2.setId(2L);
        card2.setName("Another Card");
        card2.setDescription("This is another card");
        card2.setColor("#000000");
        card2.setStatus(CardStatus.IN_PROGRESS);
        card2.setCreatedOn(LocalDateTime.now());
        card2.setUpdatedOn(LocalDateTime.now());
        card2.setUser(user);
        Pageable pageable = PageRequest.of(0, 10);
        List<CardsEntity> cardsList = Arrays.asList(card1, card2);
        Page<CardsEntity> cardsPage = new PageImpl<>(cardsList, pageable, cardsList.size());
        Mockito.when(cardRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(cardsPage);
        Page<CardResponse> result = cardService.searchCards(searchCriteria, pageable, false, user.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());
        Assertions.assertEquals(2, result.getContent().size());
    }



}
