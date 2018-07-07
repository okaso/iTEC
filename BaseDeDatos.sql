create database instituto;
use instituto;


create table usuarios(
  id int(11) NOT NULL AUTO_INCREMENT,
  login varchar(15) NOT NULL,
  contrasenia varchar(60) NOT NULL,
  nombre varchar(30) NOT NULL,
  ultimo_acceso datetime,
  rol varchar(15) NOT NULL,
  habilitado char(1) NOT NULL,
  creacion datetime NOT NULL,
  modificacion datetime,
  creado_por int(11) NOT NULL,
  modificado_por int(11),
  PRIMARY KEY (id)
);

INSERT INTO usuarios (login, contrasenia, nombre, rol, habilitado, creacion, creado_por)
VALUES ('pablo', SHA1('123456'), 'Pablo vargas', 'operador', 'S', NOW(), 1);
INSERT INTO usuarios (login, contrasenia, nombre, rol, habilitado, creacion, creado_por)
VALUES ('juan', SHA1('123456'), 'juan segovia', 'administrador', 'S', NOW(), 1);

create  table carreras(
	id int Not null auto_increment,
    nombre varchar(50)not null,
    descripcion varchar(150)not null,
    nroasignaturas int not null,
    duracion int ,
    matricula varchar(2),
    costo double(4,2),
	primary key (id)
    
);



create table asignaturas(
	id int auto_increment,
    nombre varchar(20) not null,
    descripcion varchar(150)not null,
    horasmes varchar(6)not null,	
    trimestre int not null,
    docente varchar(25),
    carrera_fk int(15) not null,
    primary key(id),
    foreign key(carrera_fk) references carreras(id)
);
create table alumnos(
	documento int (12)not null primary key,
    nombres varchar (20)not null,
    apellidos varchar(25) not null,
    fnacimiento date,
    carrera_fk int(11),
    foreign key (carrera_fk) references carreras(id)
);
