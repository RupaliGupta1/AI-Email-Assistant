package com.gemini.assistant.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-generator")
@CrossOrigin(origins = "*")
class EmailGeneratorController {

	private final EmailGeneratorService emailGeneratorService;
	
	public EmailGeneratorController(EmailGeneratorService emailGeneratorService) {
		this.emailGeneratorService = emailGeneratorService;
	}


	@PostMapping("/generate")
	public ResponseEntity<String> emailGenerator(@RequestBody EmailRequest request){
		String respone=emailGeneratorService.generatorEmailReply(request);
		return ResponseEntity.ok(respone);
	}
}
