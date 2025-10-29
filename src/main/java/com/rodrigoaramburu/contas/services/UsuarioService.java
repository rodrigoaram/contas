package com.rodrigoaramburu.contas.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.rodrigoaramburu.contas.dtos.UsuarioDTO;
import com.rodrigoaramburu.contas.dtos.UsuarioLoginDTO;
import com.rodrigoaramburu.contas.dtos.UsuarioUpdateDTO;
import com.rodrigoaramburu.contas.models.Usuario;
import com.rodrigoaramburu.contas.repositories.UsuarioRepository;
import com.rodrigoaramburu.contas.util.Hash;

import ch.qos.logback.core.joran.util.beans.BeanUtil;


@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void save(UsuarioDTO usuarioDTO) {
		var usuario = new Usuario();
		BeanUtils.copyProperties(usuarioDTO, usuario);
		
		usuario.setSenha(Hash.hash( usuario.getSenha()));
		
		this.usuarioRepository.save(usuario);
		
	}

	public List<Usuario> getAll() {
		return this.usuarioRepository.findAll();
	}
	
	public Page<Usuario> paginate(int page, String search) {
        PageRequest pageRequest = PageRequest.of(page, 30);
        if(search == null) {
        	return this.usuarioRepository.findAll(pageRequest);
        }

        return this.usuarioRepository.findByNomeOrEmail(pageRequest, search);
        
    }

	public boolean auth(UsuarioLoginDTO usuarioLogin) {

		 Optional<Usuario> usuarioO = this.usuarioRepository.findByEmail( usuarioLogin.email());
		
		 if(usuarioO.isEmpty()) {
			 return false;
		 }
		 
		 return Hash.check(usuarioO.get().getSenha(), usuarioLogin.senha());
	}

	public void delete(UUID id) {
		
		this.usuarioRepository.deleteById(id);
	}

	public Optional<Usuario> findById(UUID id) {
		return this.usuarioRepository.findById(id);
	}

	public void update(UUID id, UsuarioUpdateDTO usuarioDTO) {
		Usuario usuario = this.usuarioRepository.findById(id).get();
		BeanUtils.copyProperties(usuarioDTO, usuario, "senha");
		
		if(!usuarioDTO.senha().isBlank()) {
			usuario.setSenha(Hash.hash( usuarioDTO.senha()));
		}
		this.usuarioRepository.save(usuario);
		
	}
}
