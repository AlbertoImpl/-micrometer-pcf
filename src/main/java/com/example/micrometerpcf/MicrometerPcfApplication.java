package com.example.micrometerpcf;

import java.util.Arrays;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCircuitBreaker
@EnableScheduling
public class MicrometerPcfApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrometerPcfApplication.class, args);
	}

	@RestController
	static class TestController {

		private final DoSomething doSomething;

		public TestController(DoSomething doSomething) {this.doSomething = doSomething;}

		@GetMapping("/greetings")
		public String getGreetings() throws Exception {
			return "Hello, world" + doSomething.greetings();
		}

	}

	@Component
	static class DoSomething {

		@HystrixCommand(fallbackMethod = "defaultGreetings")
		@Scheduled(fixedDelay = 5000)
		List<String> greetings() throws Exception {
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
