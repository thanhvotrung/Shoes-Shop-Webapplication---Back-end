package com.ecom.Controller.shop;

import com.ecom.Model.dto.EmailDetails;
import com.ecom.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class EmailRestController {
    @Autowired
    EmailService emailService;

    @PostMapping("/sendemail")
    public ResponseEntity<HttpStatus> sendMail(@RequestBody EmailDetails details) {
        return emailService.sendSimpleMail(details);
    }

    @PostMapping("/sendemailsuccess")
    public ResponseEntity<HttpStatus> sendMailSuccess(@RequestBody EmailDetails details) {
        return emailService.sendSimpleMail(details);
    }
}
