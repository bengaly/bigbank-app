/**
 * 
 */
package com.bigbeng.loans.controller;

import java.util.List;

import com.bigbeng.loans.config.LoansServiceConfig;
import com.bigbeng.loans.model.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bigbeng.loans.model.Customer;
import com.bigbeng.loans.model.Loans;
import com.bigbeng.loans.repository.LoansRepository;

/**
 * @author Big Beng
 *
 */

@RestController
public class LoansController {
	private static  final Logger logger = LoggerFactory.getLogger(LoansController.class);

	@Autowired
	private LoansRepository loansRepository;

	@Autowired
	private LoansServiceConfig loansServiceConfig;

	@PostMapping("/myLoans")
	public List<Loans> getLoansDetails(@RequestHeader("bigbank-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.info("getLoansDetails method started");
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		logger.info("getLoansDetails method ended");
		if (loans != null) {
			return loans;
		} else {
			return null;
		}

	}
	@GetMapping("/loan/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(loansServiceConfig.getMsg(), loansServiceConfig.getBuildVersion(),
				loansServiceConfig.getMailDetails(), loansServiceConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}

}
