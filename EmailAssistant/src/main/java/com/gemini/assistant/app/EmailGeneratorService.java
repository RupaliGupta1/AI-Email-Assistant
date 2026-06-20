package com.gemini.assistant.app;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class EmailGeneratorService {
	
	private final WebClient webClient=WebClient.create();
	
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	



	public String generatorEmailReply(EmailRequest emailRequest) {
		//build the promt for gemini api
		String prompt=buildPrompt(emailRequest);
		//structue a request like gemini wants for request body
		Map<String, Object> requestBody=Map.of("contents",new Object[] {
					                            Map.of("parts",new Object[] {
					                            		Map.of("text",prompt)
					                            })
				                               }
			                            	);
				
		//do request with api key and url
		String response=webClient.post()
				.uri(geminiApiUrl + "?key=" + geminiApiKey)				.header("Content-Type", "application/json")
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		// extract response and return
		return extractResponseContent(response);
	}

	private String extractResponseContent(String response) {
		try {
			ObjectMapper mapper=new ObjectMapper();
			JsonNode rootNode=mapper.readTree(response);
			return rootNode.path("candidates").get(0)
					.path("content")
					.path("parts").get(0)
					.path("text")
					.asString();
		} catch (Exception e) {
			return "Error proccessing request: "+e.getMessage();
		}
	}

	private String buildPrompt(EmailRequest emailRequest) {
		// TODO Auto-generated method stub
		StringBuilder prompt=new StringBuilder();
		prompt.append("Generate a professional email reply for the following email without subject line");
		if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty()) {
			prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
		}
		prompt.append("\n Original email: \n").append(emailRequest.getEmailContent());
		return prompt.toString();
	}
}
