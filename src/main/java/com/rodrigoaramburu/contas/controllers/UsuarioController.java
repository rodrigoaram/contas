package com.rodrigoaramburu.contas.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rodrigoaramburu.contas.dtos.UsuarioDTO;
import com.rodrigoaramburu.contas.dtos.UsuarioLoginDTO;
import com.rodrigoaramburu.contas.dtos.UsuarioUpdateDTO;
import com.rodrigoaramburu.contas.models.Usuario;
import com.rodrigoaramburu.contas.services.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping({"/", "/usuarios/login"})
	public String loginForm() {
		
		return "usuarios/login";
	}
	
	@PostMapping("/usuarios/auth")
	public String auth(
			@ModelAttribute UsuarioLoginDTO usuarioLogin,
			HttpSession sessao
	) {
		
		if(this.usuarioService.auth(usuarioLogin)) {
			
			sessao.setAttribute("usuario", usuarioLogin.email());
			
			return "redirect:/lancamentos";
		}else {
			return "redirect:/usuarios/login";
		}
		
	}
	
	@GetMapping("/usuarios")
	public String getAll(
			Model model, 
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(name="search", required = false) String search) {
		Page<Usuario> usuariosPage = this.usuarioService.paginate(page-1, search);
		model.addAttribute("usuariosPage", usuariosPage);
		return "usuarios/list";
	}
	
	@GetMapping("/usuarios/create")
	public String create(Model model) {
		model.addAttribute("usuario", new UsuarioDTO(null, null, null));
		return "usuarios/create";
	}
	
	@PostMapping("/usuarios/store")
	public String store(
			@ModelAttribute("usuario") @Valid UsuarioDTO usuarioDTO,
			BindingResult result,
			Model model,
			RedirectAttributes redirAttr) {
		
		if(result.hasErrors()) {
			model.addAttribute("errorMessage", "Verifique os erros abaixo.");
			
			return "usuarios/create";
		}
		
		this.usuarioService.save(usuarioDTO);
		
		redirAttr.addFlashAttribute("successMessage", "O usuário foi salvo.");
		
		return "redirect:/usuarios";
	}
	
	@GetMapping("/usuarios/delete/{id}")
	public String delete(
			@PathVariable("id") UUID id,
			RedirectAttributes redirAttr) {
		
		
		this.usuarioService.delete(id);
		
		redirAttr.addFlashAttribute("successMessage", "O usuário foi deletado.");
		
		return "redirect:/usuarios";
	}
	
	@GetMapping("/usuarios/edit/{id}")
	public String edit(
			@PathVariable("id") UUID id,
			Model model
			) {
		
		Optional<Usuario> usuario = this.usuarioService.findById(id);
		
		model.addAttribute("usuario", usuario.get());
		
		return "usuarios/edit";
	}
	
	@PostMapping("/usuarios/update/{id}")
	public String update(
			@PathVariable("id") UUID id,
			@ModelAttribute("usuario") UsuarioUpdateDTO usuarioDTO,
			BindingResult bindingResult,
			RedirectAttributes redirAttr,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "Verifique os erros abaixo");
			return "usuarios/edit";
		}
		
		this.usuarioService.update(id, usuarioDTO);
		redirAttr.addFlashAttribute("successMessage", "O usuário foi atualizado");
		
		return "redirect:/usuarios";
	}
}
