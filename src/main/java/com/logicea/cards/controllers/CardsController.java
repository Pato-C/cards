package com.logicea.cards.controllers;

import com.logicea.cards.component.JwtTokenProvider;
import com.logicea.cards.dto.CardRequest;
import com.logicea.cards.dto.CardResponse;
import com.logicea.cards.dto.CardSearchCriteria;
import com.logicea.cards.entity.CardsEntity;
import com.logicea.cards.entity.UserEntity;
import com.logicea.cards.models.CardStatus;
import com.logicea.cards.models.UserRole;
import com.logicea.cards.service.CardService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class CardsController {

    @Autowired
    private CardService cardService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    public static Logger logger = LogManager.getLogger("com.logicea.cards");

    @PostMapping("/createCards")
    public ResponseEntity<?> createCard(@RequestBody CardRequest cardRequestDto,
                                        @RequestHeader(name="Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            if (cardRequestDto.getColor() != null && !cardRequestDto.getColor().matches("^#[0-9a-fA-F]{6}$")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid color format. Color should be 6 alphanumeric characters prefixed with a #.");
            }
            CardsEntity card = cardService.createCard(cardRequestDto, token);
            CardResponse createdCard = cardService.convertToResponse(Optional.ofNullable(card));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
        }catch (Exception e)
        {
            logger.error("Error creating cards "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't process request, please check server logs!!");
        }

    }
    @GetMapping("/searchCards")
    public ResponseEntity<?> searchCards(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder
    ) {
        try {
            String token = authorizationHeader.substring(7);
            UserEntity user = jwtTokenProvider.getUserFromToken(token);
            boolean isAdmin = user.getRole() == UserRole.Admin;
            Long userId = user.getId();
            CardSearchCriteria searchCriteria = new CardSearchCriteria();
            searchCriteria.setUserId(user.getId());
            searchCriteria.setName(name);
            searchCriteria.setColor(color);
            searchCriteria.setStatus(status);
            searchCriteria.setCreationDate(date);
            Pageable pageable;
            if (sortField != null && sortOrder != null) {
                Sort.Direction direction = Sort.Direction.fromString(sortOrder);
                Sort sort = Sort.by(direction, sortField);
                pageable = PageRequest.of(page, size, sort);
            } else {
                pageable = PageRequest.of(page, size);
            }
            Page<CardResponse> cardPage = cardService.searchCards(searchCriteria, pageable, isAdmin, userId);
            return ResponseEntity.ok(cardPage);
        } catch (Exception e) {
            logger.error("Error fetching cards "+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not process request please check server logs!!");
        }
    }

    @GetMapping("/searchUserCard")
    public ResponseEntity<CardResponse> getCardByName(@RequestParam("name") String cardName, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            UserEntity user = jwtTokenProvider.getUserFromToken(token);
            Optional<CardsEntity> card = cardService.getCardByNameAndUser(cardName, user);
            CardResponse response = cardService.convertToResponse(card);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}

