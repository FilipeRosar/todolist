package br.com.senai.tarefas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TarefasApplication {
	public static void main(String[] args) {
		SpringApplication.run(TarefasApplication.class, args);
	}
}
