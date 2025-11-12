-- 1. CONFIGURACIÓN INICIAL
DROP DATABASE IF EXISTS gestor_turnos;
CREATE DATABASE gestor_turnos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestor_turnos;

-- 2. BORRADO DE TABLAS
DROP TABLE IF EXISTS turnos;
DROP TABLE IF EXISTS medicos;
DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS usuarios;

-- 3. CREACIÓN DE TABLAS

-- Tabla 'usuarios': Base para todos los roles
CREATE TABLE usuarios (
  idUsuario INT NOT NULL AUTO_INCREMENT,
  dni INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  telefono VARCHAR(45) NULL,
  email VARCHAR(100) NOT NULL,
  pass VARCHAR(60) NOT NULL, 
  rol ENUM('MEDICO', 'PACIENTE') NOT NULL,
  PRIMARY KEY (idUsuario),
  UNIQUE INDEX uk_dni (dni ASC),
  UNIQUE INDEX uk_email (email ASC)
) ENGINE = InnoDB;

-- Tabla 'medicos': Extiende de 'usuarios'
CREATE TABLE medicos (
  idMedico INT NOT NULL AUTO_INCREMENT,
  matricula VARCHAR(100) NOT NULL,
  especialidad VARCHAR(100) NOT NULL,
  usuarios_idUsuario INT NOT NULL,
  PRIMARY KEY (idMedico),
  INDEX fk_medicos_usuarios (usuarios_idUsuario ASC),
  CONSTRAINT fk_medicos_usuarios
    FOREIGN KEY (usuarios_idUsuario)
    REFERENCES usuarios (idUsuario)
    ON DELETE CASCADE
) ENGINE = InnoDB;

-- Tabla 'pacientes': Extiende de 'usuarios'
CREATE TABLE pacientes (
  idPaciente INT NOT NULL AUTO_INCREMENT,
  obraSocial VARCHAR(100) NULL,
  usuarios_idUsuario INT NOT NULL,
  PRIMARY KEY (idPaciente),
  INDEX fk_pacientes_usuarios (usuarios_idUsuario ASC),
  CONSTRAINT fk_pacientes_usuarios
    FOREIGN KEY (usuarios_idUsuario)
    REFERENCES usuarios (idUsuario)
    ON DELETE CASCADE
) ENGINE = InnoDB;

-- Tabla 'turnos': El corazón del sistema
CREATE TABLE turnos (
  idTurno INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  motivo TEXT NULL,
  estado ENUM('PENDIENTE', 'CONFIRMADO', 'CANCELADO', 'REALIZADO') NOT NULL DEFAULT 'PENDIENTE',
  medicos_idMedico INT NOT NULL,
  pacientes_idPaciente INT NOT NULL,
  PRIMARY KEY (idTurno),
  INDEX fk_turnos_medicos (medicos_idMedico ASC),
  INDEX fk_turnos_pacientes (pacientes_idPaciente ASC),
  CONSTRAINT fk_turnos_medicos
    FOREIGN KEY (medicos_idMedico)
    REFERENCES medicos (idMedico),
  CONSTRAINT fk_turnos_pacientes
    FOREIGN KEY (pacientes_idPaciente)
    REFERENCES pacientes (idPaciente)
) ENGINE = InnoDB;


-- 4. INSERCIÓN DE DATOS DE DEMOSTRACIÓN

-- Contraseña para todos: "1234"
SET @pass_hash = '$2a$10$N9qo8uLOick.sl.81bQ.N.1/Q.P2u.L.8.W/m.s.r.D.d/D.t.a';

-- Insertamos 2 Médicos
INSERT INTO usuarios (dni, nombre, apellido, telefono, email, pass, rol) VALUES
(111111, 'Cristian', 'Fernandez', '11223344', 'med1@mail.com', @pass_hash, 'MEDICO'),
(222222, 'Laura', 'Lopez', '22334455', 'med2@mail.com', @pass_hash, 'MEDICO');

INSERT INTO medicos (matricula, especialidad, usuarios_idUsuario) VALUES
('M-111', 'Cardiología', 1),
('M-222', 'Clínica Médica', 2);

-- Insertamos 4 Pacientes
INSERT INTO usuarios (dni, nombre, apellido, telefono, email, pass, rol) VALUES
(333333, 'Carlos', 'Gomez', '33445566', 'pac1@mail.com', @pass_hash, 'PACIENTE'),
(444444, 'Maria', 'Perez', '44556677', 'pac2@mail.com', @pass_hash, 'PACIENTE'),
(555555, 'Juan', 'Suarez', '55667788', 'pac3@mail.com', @pass_hash, 'PACIENTE'),
(666666, 'Ana', 'Garcia', '66778899', 'pac4@mail.com', @pass_hash, 'PACIENTE');

INSERT INTO pacientes (obraSocial, usuarios_idUsuario) VALUES
('OSDE', 3),
('Swiss Medical', 4),
('PAMI', 5),
('Particular', 6);

-- Insertamos 4 Turnos
INSERT INTO turnos (fecha, hora, motivo, estado, medicos_idMedico, pacientes_idPaciente) VALUES
(CURDATE(), '10:00:00', 'Chequeo general', 'PENDIENTE', 1, 1),
(CURDATE() + INTERVAL 1 DAY, '11:30:00', 'Mostrar estudios', 'CONFIRMADO', 2, 2),
(CURDATE() - INTERVAL 7 DAY, '09:00:00', 'Dolor de cabeza', 'CANCELADO', 1, 3),
(CURDATE() - INTERVAL 14 DAY, '15:00:00', 'Control post-operatorio', 'REALIZADO', 2, 4);

-- 5. VERIFICACIÓN FINAL
SELECT '¡Script de setup completado! Base de datos lista con datos de demo.' AS Mensaje;
SELECT * FROM usuarios;
SELECT * FROM medicos;
SELECT * FROM pacientes;
SELECT * FROM turnos;