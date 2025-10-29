CREATE TABLE usuarios (
	id varchar(36) not null, 
	email varchar(255) unique, 
	nome varchar(255), 
	senha varchar(255), 
	primary key (id)
);

CREATE TABLE lancamentos (
	id varchar(36) not null, 
	data date, 
	descricao varchar(255), 
	tipo_lancamento varchar(255) check (tipo_lancamento in ('ENTRADA','SAIDA')), 
	valor integer, 
	usuario_id varchar(36) not null, 
	primary key (id)
);