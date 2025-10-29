package com.rodrigoaramburu.contas.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rodrigoaramburu.contas.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	Page<Usuario> findAll(Pageable pageable);
	
	Optional<Usuario> findByEmail(String email);

	@Query("SELECT u FROM Usuario u WHERE nome = :search OR email LIKE %:search%")
	Page<Usuario> findByNomeOrEmail(PageRequest pageRequest, @Param("search") String search);

}
