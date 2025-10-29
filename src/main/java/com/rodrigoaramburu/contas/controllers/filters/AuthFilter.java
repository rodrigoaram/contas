package com.rodrigoaramburu.contas.controllers.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthFilter implements Filter {
	
	private List<String> urlsPermitidas;
	
	public AuthFilter() {
		this.urlsPermitidas = Arrays.asList(
				"/",
				"/usuarios/login",
				"/usuarios/auth",
				"/style.css"
		);
	}

	@Override
	public void doFilter(
			ServletRequest request, 
			ServletResponse response, 
			FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request; 
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession sessao = httpRequest.getSession();
		
		
		if(this.urlsPermitidas.contains(httpRequest.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}
		
		if(sessao.getAttribute("usuario") != null) {
			chain.doFilter(request, response);
		}else {
			httpResponse.sendRedirect("/usuarios/login");
		}
		
		
	}

}
