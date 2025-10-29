package com.rodrigoaramburu.contas.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigoaramburu.contas.dtos.LancamentoStoreDTO;
import com.rodrigoaramburu.contas.models.Lancamento;
import com.rodrigoaramburu.contas.models.TipoLancamento;
import com.rodrigoaramburu.contas.models.Usuario;
import com.rodrigoaramburu.contas.repositories.LancamentoRepository;
import com.rodrigoaramburu.contas.repositories.UsuarioRepository;


@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuariosRepository;
	
	public void store(LancamentoStoreDTO lancamentoDTO, String usuarioEmail) {
		
		Lancamento lancamento = new Lancamento();
		
		BeanUtils.copyProperties(lancamentoDTO, lancamento);
		
		lancamento.setValor( (int) (lancamentoDTO.valor() * 100) );
		
		Usuario usuario = this.usuariosRepository.findByEmail(usuarioEmail).get();
		
		lancamento.setUsuario(usuario);
		
		this.lancamentoRepository.save(lancamento);
		
	}
	
	
	public List<LocalDate> listMonths(String emailUsuario){
		LocalDate now = LocalDate.now();
		
		Usuario usuario = this.usuariosRepository.findByEmail(emailUsuario).get();
		LocalDate inicio = this.lancamentoRepository.firstLancamentoData(usuario);
		
		if(inicio == null) {
			return new ArrayList<>();
		}
		
		List<LocalDate> meses = new ArrayList<LocalDate>();
		
		while(!now.isBefore(inicio)) {
			meses.add(now);
			now = now.minusMonths(1);
		}
		return meses;
	}


	public List<Lancamento> findLancamentosPorMes(LocalDate dataMes, String emailUsuario) {

		LocalDate inicio = dataMes.withDayOfMonth(1);
		LocalDate fim = dataMes.withDayOfMonth(dataMes.lengthOfMonth());
		
		Usuario usuario = this.usuariosRepository.findByEmail(emailUsuario).get();
		
		List<Lancamento> lancamentos = this.lancamentoRepository.findLancamentoEntre(inicio, fim, usuario);

		return lancamentos;
	}
	
	public Integer totalSaldo(LocalDate dataMes, String emailUsuario) {
		
		LocalDate fim = dataMes.withDayOfMonth(dataMes.lengthOfMonth());
		
		Usuario usuario = this.usuariosRepository.findByEmail(emailUsuario).get();
		
		Integer saldo = this.lancamentoRepository.totalSaldo(fim, usuario);

		return saldo ;
	}
	
	
	public Map<String, Integer> balancoMes(LocalDate dataMes, String emailUsuario){
		
		LocalDate inicio = dataMes.withDayOfMonth(1);
		LocalDate fim = dataMes.withDayOfMonth(dataMes.lengthOfMonth());
		
		Usuario usuario = this.usuariosRepository.findByEmail(emailUsuario).get();
		
		List<Lancamento> lancamentos = this.lancamentoRepository.findLancamentoEntre(inicio, fim, usuario);
		
		Map<String,Integer> balancoMes = new HashMap<String, Integer>();
		
		Integer entradas = lancamentos.stream()
				.filter( l -> l.getTipoLancamento() == TipoLancamento.ENTRADA)
				.map( ( Lancamento l ) -> l.getValor() )
				.reduce(0, (a , b) ->  a + b );
		
		Integer saidas = lancamentos.stream()
				.filter( l -> l.getTipoLancamento() == TipoLancamento.SAIDA)
				.map( ( Lancamento l ) -> l.getValor() )
				.reduce(0, (a , b) ->  a + b );
		
		
		balancoMes.put("ENTRADA", entradas);
		balancoMes.put("SAIDA", saidas);
		
		return balancoMes;
	}


	public void delete(UUID id) {
		this.lancamentoRepository.deleteById(id);		
	}
	
}
