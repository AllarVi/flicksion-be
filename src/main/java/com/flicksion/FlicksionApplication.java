package com.flicksion;

import com.flicksion.greeting.GreetingWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlicksionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlicksionApplication.class, args);

		GreetingWebClient gwc = new GreetingWebClient();
		System.out.println(gwc.getResult());
	}
}
