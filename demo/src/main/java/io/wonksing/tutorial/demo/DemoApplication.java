package io.wonksing.tutorial.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{
	MonoDemo md = new MonoDemo();
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		md.just();

		md.justOrEmpty();

		md.switchIfEmpty();

		md.fromSupplier();

		try {
			md.error();
		}catch(Exception e) {
			e.printStackTrace();
		}

		md.onErrorResume();
		md.onErrorReturn();

		md.map();

		md.flatMap();
	}
}
