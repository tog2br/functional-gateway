/**
 * 
 */
package com.example.functional.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.functional.gateway.FunctionalGateway;

import reactor.core.publisher.Mono;

/**
 * Service to call functional gateway
 * 
 * @author Thiago de Luca
 */
@Service
public class ServiceExample {
	
	private final FunctionalGateway functionalGateway;
	private final String swapiBaseUrl;
	
	/**
	 * Default constructor
	 * 
	 * @param functionalGateway Inject the FunctionalGateway
	 * @param swapiBaseUrl The Star Wars base Url
	 */
	@Autowired	
	public ServiceExample(FunctionalGateway functionalGateway,
						  @Value("${swapi.dev.base.url}") String swapiBaseUrl) {
		this.functionalGateway = functionalGateway;
		this.swapiBaseUrl = swapiBaseUrl;
	}

	/**
	 * Method to find a people of Star Wars API
	 * 
	 * @param peopleCode the id of people... 1,2...
	 * @return {@link Mono}
	 */
	@SuppressWarnings("unchecked")
	public Mono<String> getPeopleStarWars(int peopleCode) {
		var uri = UriComponentsBuilder.fromUriString(this.swapiBaseUrl).path("/people/").path(String.valueOf(peopleCode)).build().toUriString();
		return functionalGateway.doRequestReturnMono(uri, null, HttpMethod.GET, String.class);
	}
}