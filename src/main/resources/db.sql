create table usuario (
	id bigint not null auto_increment,
	nome varchar(100) not null,
	login varchar(50) not null,
	senha varchar(100) not null,
	ativo boolean default false,
	primary key (id),
	unique key un_login (login)
);

insert into usuario values (1, 'Carlos', 'carlos', '$2a$10$JvyF9q/k/eYwXTVjc4Ay0OT/dCwjW14eT88q3e587jaENTvtt30s2', true), (2, 'Flávio', 'flavio', '$2a$10$JvyF9q/k/eYwXTVjc4Ay0OT/dCwjW14eT88q3e587jaENTvtt30s2', true), (3, 'Alexandre Afonso', 'alexandre', '$2a$10$JvyF9q/k/eYwXTVjc4Ay0OT/dCwjW14eT88q3e587jaENTvtt30s2', true);

create table usuario_permissao (
	usuario_id bigint not null,
	permissao varchar(50) not null,
	primary key (usuario_id, permissao),
	constraint fk_usuariopermissao_usuario foreign key (usuario_id) references usuario(id)
);

insert into usuario_permissao values (1, 'ROLE_PG_CUSTOS'), (1, 'ROLE_PG_EQUIPE'), (2, 'ROLE_PG_EQUIPE'), (3, 'ROLE_USUARIO');
