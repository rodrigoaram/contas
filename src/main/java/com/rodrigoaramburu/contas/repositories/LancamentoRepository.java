package com.rodrigoaramburu.contas.repositories;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rodrigoaramburu.contas.models.Lancamento;
import com.rodrigoaramburu.contas.models.Usuario;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, UUID>{

	@Query("SELECT MIN(data) FROM Lancamento WHERE usuario = :usuario")
	LocalDate firstLancamentoData(@Param("usuario") Usuario usuario);

	@Query("SELECT l FROM Lancamento l WHERE data BETWEEN :inicio AND :fim AND usuario = :usuario ORDER BY data ASC")
	List<Lancamento> findLancamentoEntre(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim, @Param("usuario") Usuario usuario);

	@Query(" SELECT (SELECT COALESCE( SUM(l.valor), 0 ) FROM Lancamento l WHERE tipoLancamento = 'ENTRADA' AND data < :dataFim AND usuario = :usuario) - (SELECT COALESCE(SUM(l.valor),0) FROM Lancamento l WHERE tipoLancamento = 'SAIDA' AND data < :dataFim AND usuario = :usuario)")
	Integer totalSaldo(@Param("dataFim") LocalDate dataFim, @Param("usuario") Usuario usuario);
	
	
}
