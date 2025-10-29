package com.rodrigoaramburu.contas.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
		@NotBlank(message="O nome deve ser preenchido")
		String nome,
		
		@NotBlank(message="O e-mail deve ser preenchido")
		@Email(message="O e-mail deve ser válido")
		String email,
		
		@Size(min = 6, message="A senha deve ter no mínimo 6 caracteres")
		String senha
		) {

}
