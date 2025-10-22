
CREATE DATABASE IF NOT EXISTS `gestor_turnos` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `gestor_turnos`;

DROP TABLE IF EXISTS `turnos`;
DROP TABLE IF EXISTS `medicos`;
DROP TABLE IF EXISTS `pacientes`;
DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `dni` INT NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `pass` VARCHAR(100) NOT NULL,
  `rol` ENUM('MEDICO', 'PACIENTE') NOT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `dni_UNIQUE` (`dni`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `medicos` (
  `idMedico` INT NOT NULL AUTO_INCREMENT,
  `matricula` VARCHAR(45) NOT NULL,
  `especialidad` VARCHAR(100) NOT NULL,
  `usuarios_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idMedico`),
  UNIQUE KEY `matricula_UNIQUE` (`matricula`),
  KEY `fk_medicos_usuarios_idx` (`usuarios_idUsuario`),
  CONSTRAINT `fk_medicos_usuarios` FOREIGN KEY (`usuarios_idUsuario`) REFERENCES `usuarios` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `pacientes` (
  `idPaciente` INT NOT NULL AUTO_INCREMENT,
  `obraSocial` VARCHAR(100) NOT NULL,
  `usuarios_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idPaciente`),
  KEY `fk_pacientes_usuarios1_idx` (`usuarios_idUsuario`),
  CONSTRAINT `fk_pacientes_usuarios1` FOREIGN KEY (`usuarios_idUsuario`) REFERENCES `usuarios` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `turnos` (
  `idTurno` INT NOT NULL AUTO_INCREMENT,
  `fecha` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `motivo` TEXT NOT NULL,
  `estado` ENUM('PENDIENTE', 'CONFIRMADO', 'CANCELADO', 'REALIZADO') NOT NULL DEFAULT 'PENDIENTE',
  `medicos_idMedico` INT NOT NULL,
  `pacientes_idPaciente` INT NOT NULL,
  PRIMARY KEY (`idTurno`),
  KEY `fk_turnos_medicos1_idx` (`medicos_idMedico`),
  KEY `fk_turnos_pacientes1_idx` (`pacientes_idPaciente`),
  CONSTRAINT `fk_turnos_medicos1` FOREIGN KEY (`medicos_idMedico`) REFERENCES `medicos` (`idMedico`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_turnos_pacientes1` FOREIGN KEY (`pacientes_idPaciente`) REFERENCES `pacientes` (`idPaciente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


START TRANSACTION;

INSERT INTO `usuarios` (`dni`, `nombre`, `apellido`, `telefono`, `email`, `pass`, `rol`)
VALUES ('38818356','Heber','Ramirez','3482605057','heber96nr@gmail.com','605057Heber','MEDICO');
SET @idUsuarioHeber = LAST_INSERT_ID();
INSERT INTO `medicos` (`matricula`, `especialidad`, `usuarios_idUsuario`)
VALUES ('UNNE2025','Cardiólogo', @idUsuarioHeber);
SET @idMedicoHeber = LAST_INSERT_ID();

INSERT INTO `usuarios` (`dni`, `nombre`, `apellido`, `telefono`, `email`, `pass`, `rol`)
VALUES ('42548795','Hector','Gomez','3482605057','hector@paciente.com','3818356','PACIENTE');
SET @idUsuarioHector = LAST_INSERT_ID();
INSERT INTO `pacientes` (`obraSocial`, `usuarios_idUsuario`)
VALUES ('OSDE', @idUsuarioHector);
SET @idPacienteHector = LAST_INSERT_ID();

INSERT INTO `usuarios` (`dni`, `nombre`, `apellido`, `telefono`, `email`, `pass`, `rol`)
VALUES ('30123456','Maria','Luna','341123456','maria@paciente.com','123456','PACIENTE');
SET @idUsuarioMaria = LAST_INSERT_ID();
INSERT INTO `pacientes` (`obraSocial`, `usuarios_idUsuario`)
VALUES ('PAMI', @idUsuarioMaria);
SET @idPacienteMaria = LAST_INSERT_ID();

INSERT INTO `turnos` (`fecha`, `hora`, `motivo`, `estado`, `medicos_idMedico`, `pacientes_idPaciente`)
VALUES
(CURDATE(), '09:00:00', 'Control de rutina', 'PENDIENTE', @idMedicoHeber, @idPacienteHector),
(CURDATE(), '09:30:00', 'Presenta arritmia', 'CONFIRMADO', @idMedicoHeber, @idPacienteMaria),
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', 'Revisión pre-quirúrgica', 'PENDIENTE', @idMedicoHeber, @idPacienteHector);

COMMIT;
