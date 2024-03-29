/**
 * 
 */
package com.bigbeng.cards.controller;

import java.util.List;

import com.bigbeng.cards.config.CardsServiceConfig;
import com.bigbeng.cards.model.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bigbeng.cards.model.Cards;
import com.bigbeng.cards.model.Customer;
import com.bigbeng.cards.repository.CardsRepository;

/**
 * @author Big Beng
 *
 */

@RestController
public class CardsController {
	private static  final Logger logger = LoggerFactory.getLogger(CardsController.class);
	@Autowired
	private CardsRepository cardsRepository;

	@Autowired
	private CardsServiceConfig cardsServiceConfig;

	@PostMapping("/myCards")
	public List<Cards> getCardDetails(@RequestHeader("bigbank-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.info("getCardDetails method started");
		List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		logger.info("getCardDetails method ended");
		if (cards != null) {
			return cards;
		} else {
			return null;
		}

	}
	@GetMapping("/card/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(cardsServiceConfig.getMsg(), cardsServiceConfig.getBuildVersion(),
				cardsServiceConfig.getMailDetails(), cardsServiceConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}

}
