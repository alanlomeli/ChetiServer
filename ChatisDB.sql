-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 04, 2019 at 04:23 AM
-- Server version: 10.4.6-MariaDB-log
-- PHP Version: 7.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ChatisDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `Amistad`
--

CREATE TABLE `Amistad` (
  `Amistad_ID` int(11) NOT NULL,
  `Persona_FK` bigint(255) DEFAULT NULL,
  `Amigo_FK` bigint(255) DEFAULT NULL,
  `Apodo` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Amistad`
--

INSERT INTO `Amistad` (`Amistad_ID`, `Persona_FK`, `Amigo_FK`, `Apodo`) VALUES
(1, 666, 777, 'richie'),
(2, 777, 666, 'niño');

-- --------------------------------------------------------

--
-- Table structure for table `Grupo`
--

CREATE TABLE `Grupo` (
  `Grupo_ID` int(11) NOT NULL,
  `Nombre` varchar(25) DEFAULT NULL,
  `Creador_FK` bigint(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Integrantes_Grupo`
--

CREATE TABLE `Integrantes_Grupo` (
  `Integrantes_Grupo_ID` int(11) NOT NULL,
  `Grupo_FK` int(255) DEFAULT NULL,
  `Usuario_FK` bigint(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Mensajes_Pendientes`
--

CREATE TABLE `Mensajes_Pendientes` (
  `Mensajes_Pendientes_ID` int(11) NOT NULL,
  `Integrantes_Grupo_FK` int(255) DEFAULT NULL,
  `Persona_Sin_Leer` bigint(255) DEFAULT NULL,
  `Mensaje` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Solicitudes_Grupo`
--

CREATE TABLE `Solicitudes_Grupo` (
  `Solicitudes_Grupo_ID` int(11) NOT NULL,
  `Grupo_FK` int(255) DEFAULT NULL,
  `Usuario_FK` bigint(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Usuario`
--

CREATE TABLE `Usuario` (
  `Celular` bigint(255) NOT NULL,
  `Nombre` varchar(30) DEFAULT NULL,
  `Apellido` varchar(30) DEFAULT NULL,
  `Respuesta` varchar(30) DEFAULT NULL,
  `Passwd` blob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Usuario`
--

INSERT INTO `Usuario` (`Celular`, `Nombre`, `Apellido`, `Respuesta`, `Passwd`) VALUES
(666, 'Alans', 'Lomelangass', 'Caquita', 0x8803c0645a48912427f322a17174cf4c),
(777, 'RIchie', 'caca', 'caca', 0x8803c0645a48912427f322a17174cf4c),
(3323, 'cacas', 'cacs', 'yeah', 0x8803c0645a48912427f322a17174cf4c),
(3336266817, 'Alan Jesus', 'Lomeli Garcia', 'Guadalajara', 0x8803c0645a48912427f322a17174cf4c),
(3336266818, 'Alancin', 'Dos', 'GDL', 0x8803c0645a48912427f322a17174cf4c),
(3336266819, 'Doug Dimmadome', 'DUeño del Dommodim', 'Dimmsdale', 0x8803c0645a48912427f322a17174cf4c),
(3336266917, 'Lizzie ', 'Grant', 'New York', 0x8803c0645a48912427f322a17174cf4c);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Amistad`
--
ALTER TABLE `Amistad`
  ADD PRIMARY KEY (`Amistad_ID`),
  ADD KEY `Persona_FK` (`Persona_FK`),
  ADD KEY `Amigo_FK` (`Amigo_FK`);

--
-- Indexes for table `Grupo`
--
ALTER TABLE `Grupo`
  ADD PRIMARY KEY (`Grupo_ID`),
  ADD KEY `Creador_FK` (`Creador_FK`);

--
-- Indexes for table `Integrantes_Grupo`
--
ALTER TABLE `Integrantes_Grupo`
  ADD PRIMARY KEY (`Integrantes_Grupo_ID`),
  ADD KEY `Grupo_FK` (`Grupo_FK`),
  ADD KEY `Usuario_FK` (`Usuario_FK`);

--
-- Indexes for table `Mensajes_Pendientes`
--
ALTER TABLE `Mensajes_Pendientes`
  ADD PRIMARY KEY (`Mensajes_Pendientes_ID`),
  ADD KEY `Integrantes_Grupo_FK` (`Integrantes_Grupo_FK`),
  ADD KEY `Persona_Sin_Leer` (`Persona_Sin_Leer`);

--
-- Indexes for table `Solicitudes_Grupo`
--
ALTER TABLE `Solicitudes_Grupo`
  ADD PRIMARY KEY (`Solicitudes_Grupo_ID`),
  ADD KEY `Grupo_FK` (`Grupo_FK`),
  ADD KEY `Usuario_FK` (`Usuario_FK`);

--
-- Indexes for table `Usuario`
--
ALTER TABLE `Usuario`
  ADD PRIMARY KEY (`Celular`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Amistad`
--
ALTER TABLE `Amistad`
  MODIFY `Amistad_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `Grupo`
--
ALTER TABLE `Grupo`
  MODIFY `Grupo_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `Integrantes_Grupo`
--
ALTER TABLE `Integrantes_Grupo`
  MODIFY `Integrantes_Grupo_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `Mensajes_Pendientes`
--
ALTER TABLE `Mensajes_Pendientes`
  MODIFY `Mensajes_Pendientes_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `Solicitudes_Grupo`
--
ALTER TABLE `Solicitudes_Grupo`
  MODIFY `Solicitudes_Grupo_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Amistad`
--
ALTER TABLE `Amistad`
  ADD CONSTRAINT `Amistad_ibfk_1` FOREIGN KEY (`Persona_FK`) REFERENCES `Usuario` (`Celular`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Amistad_ibfk_2` FOREIGN KEY (`Amigo_FK`) REFERENCES `Usuario` (`Celular`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Grupo`
--
ALTER TABLE `Grupo`
  ADD CONSTRAINT `Grupo_ibfk_1` FOREIGN KEY (`Creador_FK`) REFERENCES `Usuario` (`Celular`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Integrantes_Grupo`
--
ALTER TABLE `Integrantes_Grupo`
  ADD CONSTRAINT `Integrantes_Grupo_ibfk_1` FOREIGN KEY (`Grupo_FK`) REFERENCES `Grupo` (`Grupo_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Integrantes_Grupo_ibfk_2` FOREIGN KEY (`Usuario_FK`) REFERENCES `Usuario` (`Celular`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Mensajes_Pendientes`
--
ALTER TABLE `Mensajes_Pendientes`
  ADD CONSTRAINT `Mensajes_Pendientes_ibfk_1` FOREIGN KEY (`Integrantes_Grupo_FK`) REFERENCES `Integrantes_Grupo` (`Integrantes_Grupo_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Mensajes_Pendientes_ibfk_2` FOREIGN KEY (`Persona_Sin_Leer`) REFERENCES `Usuario` (`Celular`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Solicitudes_Grupo`
--
ALTER TABLE `Solicitudes_Grupo`
  ADD CONSTRAINT `Solicitudes_Grupo_ibfk_1` FOREIGN KEY (`Grupo_FK`) REFERENCES `Grupo` (`Grupo_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Solicitudes_Grupo_ibfk_2` FOREIGN KEY (`Usuario_FK`) REFERENCES `Usuario` (`Celular`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
