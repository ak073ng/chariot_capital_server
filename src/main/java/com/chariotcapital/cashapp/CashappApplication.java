package com.chariotcapital.cashapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

@Controller
@SpringBootApplication
public class CashappApplication {

	@GetMapping("/")
	@ResponseBody
	String home() {
		return "CC server is running.";
	}

	@GetMapping(value = "/privacy-policy")
	public StreamingResponseBody getStreamingFile(HttpServletResponse response) throws URISyntaxException {

		File file = new File(getClass().getClassLoader().getResource("templates/cc-privacy-policy.pdf").toExternalForm());

		//viewing in web browser
		response.setContentType("application/pdf");
		//for downloading the file directly if viewing is not possible
		response.setHeader("Content-Disposition", "inline; filename=" + file.getName());

		file = null;

		//put the directory architecture according to your target directory
		// generated during compilation in maven spring boot
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/cc-privacy-policy.pdf");

		return outputStream -> {
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				outputStream.write(data, 0, nRead);
			}
			inputStream.close();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(CashappApplication.class, args);
	}

}