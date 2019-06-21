package com.example.micrometerpcf;

import java.util.Arrays;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCircuitBreaker
public class MicrometerPcfApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrometerPcfApplication.class, args);
	}

	@RestController
	class TestController {

		@Autowired
		private DoSomething doSomething;

		@GetMapping("/greetings")
		public String getGreetings() throws Exception {
			return "Hello, world" + doSomething.greetings();
		}

	}

	@Component
	class DoSomething {

		@HystrixCommand(fallbackMethod = "defaultGreetings")
		public List<String> greetings() throws Exception {
			double random = Math.random();
			if (random > 0.5) {
				throw new RuntimeException();
			}
			if (random < 0.15) {
				Thread.sleep(Double.valueOf(random * 6000).intValue());
			}
			return Arrays.asList("A", "B");
		}

		public List<String> defaultGreetings() {
			return Arrays.asList("C", "D");
		}
	}

}
