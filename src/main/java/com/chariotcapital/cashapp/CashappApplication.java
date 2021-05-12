package com.chariotcapital.cashapp;

import com.chariotcapital.cashapp.utility.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class CashappApplication {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Chariot Capital server is running! - test 10";
	}

	public static void main(String[] args) {
		SpringApplication.run(CashappApplication.class, args);
	}

}
