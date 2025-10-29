package com.rodrigoaramburu.contas.dtos;

import java.time.LocalDate;

import com.rodrigoaramburu.contas.models.TipoLancamento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LancamentoStoreDTO(

		String descricao,
		
		@NotNull(message = "A data do lançamento deve ser informado")
		LocalDate data,
		
		@NotNull
		TipoLancamento tipoLancamento,
		
		@Min(value = 0)
		@NotNull
		Float valor
		
) {

}
