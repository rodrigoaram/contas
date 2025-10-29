package com.rodrigoaramburu.contas.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rodrigoaramburu.contas.dtos.LancamentoStoreDTO;
import com.rodrigoaramburu.contas.models.Lancamento;
import com.rodrigoaramburu.contas.services.LancamentoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LancamentoController {
	
	@Autowired
	private LancamentoService lancamentoService;
	
	
	@GetMapping("/lancamentos")
	public String listLancamento(
			Model model, 
			@RequestParam( name = "data", required = false) LocalDate dataMes,
			HttpSession sessao) {
		
		if(dataMes == null) {
			dataMes = LocalDate.now().withDayOfMonth(1);
		}
		
		String emailUsuario = (String) sessao.getAttribute("usuario");
		
		List<Lancamento> lancamentos = this.lancamentoService.findLancamentosPorMes(dataMes, emailUsuario);
		
		model.addAttribute("meses", this.lancamentoService.listMonths(emailUsuario));
		model.addAttribute("saldo", this.lancamentoService.totalSaldo(dataMes, emailUsuario));
		model.addAttribute("balanco", this.lancamentoService.balancoMes(dataMes, emailUsuario));
		model.addAttribute("lancamentos", lancamentos);
		return "lancamentos/list";
	}
	
	@GetMapping("/lancamentos/create")
	public String createLancamento(Model model) {
		
		model.addAttribute("hoje", LocalDate.now());
		model.addAttribute("lancamento", new Lancamento());
		
		return "lancamentos/create";
	}
	
	@PostMapping("/lancamentos/store")
	public String storeLancamento(
			Model mode,
			@Valid @ModelAttribute("lancamento") LancamentoStoreDTO lancamento,
			BindingResult bindingResult,
			HttpSession sessao,
			Model model,
			RedirectAttributes redirAttr
	) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "Verifique os erros abaixo");
			return "lancamentos/create";
		}
		
		String usuarioEmail = (String) sessao.getAttribute("usuario");
		this.lancamentoService.store(lancamento, usuarioEmail);
		
		redirAttr.addFlashAttribute("successMessage", "O lançamento foi adicionado");
		
		return "redirect:/lancamentos";
	}
	
	
	@GetMapping("/lancamentos/{id}/delete")
	public String deleteLancamento(@PathVariable("id") UUID id,RedirectAttributes redirAttr) {
		
		
		this.lancamentoService.delete(id);
		
		redirAttr.addFlashAttribute("successMessage", "O lançamento foi excluído com sucesso");
		
		return "redirect:/lancamentos";
		
	}

}
