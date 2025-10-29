package com.rodrigoaramburu.contas;

import java.util.Scanner;

import com.rodrigoaramburu.contas.util.Hash;

public class Test {

	public static void main(String[] args) {

		String hash = Hash.hash("rd1234");
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Informe senha: ");
		String senha = scan.nextLine();
		
		if( Hash.check(hash, senha)) {
			System.out.println("Senha correta");
		}else {
			System.out.println("Senha incorreta");
		}
			

	}

}
