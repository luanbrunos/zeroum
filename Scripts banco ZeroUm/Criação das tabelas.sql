-- Tabela Usuário
CREATE TABLE usuario(
	id_usuario int,
	email varchar(50),
	nome varchar(80),
	senha varchar(10),
	pesoClasse1 float,
	pesoClasse2 float,
	pesoClasse3 float
);

COMMENT ON COLUMN usuario.id_usuario IS 'Chave primária da tabela';
COMMENT ON COLUMN usuario.email IS 'Email do usuário, usado pra login';
COMMENT ON COLUMN usuario.nome IS 'Nome do usuário';
COMMENT ON COLUMN usuario.senha IS 'Senha do usuário';
COMMENT ON COLUMN usuario.pesoClasse1 IS 'Peso do usuário para a classe 1';
COMMENT ON COLUMN usuario.pesoClasse2 IS 'Peso do usuário para a classe 2';
COMMENT ON COLUMN usuario.pesoClasse3 IS 'Peso do usuário para a classe 3';

ALTER TABLE usuario
ADD CONSTRAINT id_usuario_pk
PRIMARY KEY(id_usuario); 



-- Tabela Classe
CREATE TABLE classe(
	id_classe int,
	titulo varchar(50)
);

COMMENT ON COLUMN classe.id_classe IS 'Chave primária da tabela';
COMMENT ON COLUMN classe.titulo IS 'Título da classe';

ALTER TABLE classe
ADD CONSTRAINT id_classe_pk
PRIMARY KEY(id_classe); 


-- Tabela Deputado
CREATE TABLE deputado(
	id_deputado int,
	nome varchar(60)
); 

COMMENT ON COLUMN deputado.id_deputado IS 'Chave primária da tabela';
COMMENT ON COLUMN deputado.nome IS 'Nome do deputado';

ALTER TABLE deputado
ADD CONSTRAINT id_deputado_pk
PRIMARY KEY(id_deputado);


-- Tabela Notícia
CREATE TABLE noticia(
	id_noticia int,
	titulo varchar(100),
	texto varchar,
	link varchar(100),
	id_deputado int,
	id_classe int,
	data timestamp,
	ativo boolean DEFAULT true
);


COMMENT ON COLUMN noticia.id_noticia IS 'Chave primária da tabela';
COMMENT ON COLUMN noticia.titulo IS 'Título da notícia';
COMMENT ON COLUMN noticia.texto IS 'Texto da notícia';
COMMENT ON COLUMN noticia.link IS 'Link da notícia na página';
COMMENT ON COLUMN noticia.id_deputado IS 'Deputado autor da notícia';
COMMENT ON COLUMN noticia.id_classe IS 'Classe a qual a notícia pertence';
COMMENT ON COLUMN noticia.data IS 'Data que a notícia foi publicada';
COMMENT ON COLUMN noticia.ativo IS 'Define se a notícia está ativa';

ALTER TABLE noticia
ADD CONSTRAINT id_noticia_pk
PRIMARY KEY(id_noticia);

ALTER TABLE noticia
ADD CONSTRAINT id_deputado_fk
FOREIGN KEY(id_deputado) REFERENCES deputado;

ALTER TABLE noticia
ADD CONSTRAINT id_classe_fk
FOREIGN KEY(id_classe) REFERENCES classe;

-- Tabela Usuário-Notícia (relaciona um usuário a uma notícia, definindo seu julgamento)
CREATE TABLE usuario_noticia(
	id_usuario_noticia int,
	id_usuario int,
	id_noticia int,
	relevancia int,
	ativa boolean DEFAULT true,
	lida boolean DEFAULT false
);

COMMENT ON COLUMN usuario_noticia.id_usuario_noticia IS 'Chave primária da tabela';
COMMENT ON COLUMN usuario_noticia.id_usuario IS 'Usuário que julgou a notícia';
COMMENT ON COLUMN usuario_noticia.id_noticia IS 'Notícia sendo julgada';
COMMENT ON COLUMN usuario_noticia.relevancia IS 'Relevância da notícia definida pela usuário';
COMMENT ON COLUMN usuario_noticia.ativa IS 'Define se o registro está ativo';
COMMENT ON COLUMN usuario_noticia.lida IS 'Define se a notícia foi lida pelo usuário [campo morto]';

ALTER TABLE usuario_noticia
ADD CONSTRAINT id_usuario_noticia_pk
PRIMARY KEY(id_usuario_noticia);

ALTER TABLE usuario_noticia
ADD CONSTRAINT id_usuario_fk
FOREIGN KEY(id_usuario) REFERENCES usuario;

ALTER TABLE usuario_noticia
ADD CONSTRAINT id_noticia_fk
FOREIGN KEY(id_noticia) REFERENCES noticia; 