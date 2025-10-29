package com.rodrigoaramburu.contas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rodrigoaramburu.contas.util.Hash;

@SpringBootApplication
public class ContasApplication {

	public static void main(String[] args) {

		System.out.println(Hash.hash( "123456"));
		SpringApplication.run(ContasApplication.class, args);
	}
	

}
