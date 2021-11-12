package com.workshop.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyRestService {

	@Autowired
	private RestTemplate myRestTemplate;

	@Value("${myrest.url}")
	private String restUrl;

	// GET /api/original
	public Developer getDeveloper() {

		String finalUrl = restUrl + "/original";
		log.info("GET to " + finalUrl);
		return myRestTemplate.getForObject(finalUrl, Developer.class);

	}

	// GET /api/getjwtsecret
	public String getDeveloperAsJWTwithSecret() {

		String finalUrl = restUrl + "/getjwtsecret";
		log.info("GET to " + finalUrl);
		JwtResponse response = myRestTemplate.getForObject(finalUrl, JwtResponse.class);
		return response.getJwt();

	}

	// GET /api/getjwtprivate
	public String getDeveloperAsJWTwithPrivate() {

		String finalUrl = restUrl + "/getjwtprivate";
		log.info("GET to " + finalUrl);
		JwtResponse response = myRestTemplate.getForObject(finalUrl, JwtResponse.class);
		return response.getJwt();

	}

	// GET /api/receiveparam?jwt=XXX
	public void sendJwtInQueryParameter(String jwt) {
		
		String urlToUse = restUrl + "/receiveparam";

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlToUse).queryParam("jwt", jwt);

		String finalUrl = uriBuilder.toUriString();
		log.info("GET to " + finalUrl);

		myRestTemplate.getForObject(finalUrl, String.class);

	}

	// GET /api/receiveheader
	public void sendJwtInHeader(String jwt) {
		
	    String finalUrl = restUrl + "/receiveheader";
	    log.info("GET to " + finalUrl);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", jwt);  

	    HttpEntity<String> requestEntity = new HttpEntity<>("null",headers);
	    myRestTemplate.exchange(finalUrl, HttpMethod.GET, requestEntity, String.class);
		
	}
	
	// POST /api/receivebody
	public void sendJwtInBody(String jwt) {
		
	    String finalUrl = restUrl + "/receivebody";
	    log.info("POST to " + finalUrl);
	    
	    JwtResponse res = new JwtResponse(jwt);
	    
	    myRestTemplate.postForObject(finalUrl, res, JwtResponse.class);
		
	}

}
