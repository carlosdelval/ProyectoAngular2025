-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 25, 2025 at 06:58 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurante`
--

-- --------------------------------------------------------

--
-- Table structure for table `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) NOT NULL,
  `fecha_registro` datetime(6) DEFAULT NULL,
  `rol` varchar(255) NOT NULL DEFAULT 'cliente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `clientes`
--

INSERT INTO `clientes` (`id`, `username`, `password`, `nombre`, `email`, `telefono`, `fecha_registro`, `rol`) VALUES
(1, 'carlos', '$2y$10$ClB6pNzo5aFcZ9M7T.2c9.cQYxmSjv0IijU7oV4g5t0o59FCjHWpa', 'Carlos López', 'clopval489@iesmarquesdecomares.org', '664138114', NULL, 'cliente'),
(16, 'pepillo', 'pepillo', 'Pepepepillo', 'hola@gmail.com', '979745909', '2025-02-20 08:30:59.000000', 'cliente'),
(17, 'carlillos', 'carlillos', 'CarlitosBrown', 'carlospnt96@gmail.com', '555555555', '2025-02-20 08:32:19.000000', 'cliente'),
(34, 'carlillos2', 'carlillos2', 'CarliñosBrown', 'hola2@gmail.com', '979745909', '2025-02-23 16:27:13.000000', 'cliente'),
(36, 'admin', 'admin', 'admin', 'admin@admin.com', '66445587', NULL, 'admin'),
(37, 'hola', 'hola', 'hola holita', 'holamaquina@gmail.com', '979745909', '2025-02-24 08:18:29.000000', 'cliente'),
(38, 'buenosdias', 'buenosdias', 'buenas', 'buenas@gmail.com', '979745909', '2025-02-25 13:33:01.000000', 'cliente'),
(39, 'miguel', 'miguel', 'Miguel', 'miguel@gmail.com', '66666666', '2025-02-25 13:44:53.000000', 'cliente');

-- --------------------------------------------------------

--
-- Table structure for table `cocineros`
--

CREATE TABLE `cocineros` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `especialidad` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cocineros`
--

INSERT INTO `cocineros` (`id`, `nombre`, `especialidad`) VALUES
(1, 'Juan Martinez', 'Chef'),
(2, 'Joselito Cocinillas', 'Pinche'),
(3, 'Rodolfo \"Chorizo Candente\" Gutierrez', 'Brasa'),
(4, 'Jose María Gutierrez \"Guti\"', 'Cocido Madrileño Real');

-- --------------------------------------------------------

--
-- Table structure for table `cocineros_platos`
--

CREATE TABLE `cocineros_platos` (
  `cocinero_id` int(11) NOT NULL,
  `plato_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cocineros_platos`
--

INSERT INTO `cocineros_platos` (`cocinero_id`, `plato_id`) VALUES
(1, 2),
(1, 3),
(2, 1),
(2, 3),
(2, 5),
(3, 2),
(3, 7),
(4, 5),
(4, 6);

-- --------------------------------------------------------

--
-- Table structure for table `pedidos`
--

CREATE TABLE `pedidos` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `plato_id` int(11) NOT NULL,
  `total` double NOT NULL,
  `fecha_pedido` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pedidos`
--

INSERT INTO `pedidos` (`id`, `cliente_id`, `plato_id`, `total`, `fecha_pedido`) VALUES
(1, 36, 6, 11.5, '2025-02-25 10:05:02.000000'),
(2, 17, 6, 11.5, '2025-02-25 10:05:52.000000'),
(3, 17, 3, 11.9, '2025-02-25 12:59:57.000000'),
(4, 16, 6, 11.5, '2025-02-25 13:43:37.000000'),
(5, 16, 6, 11.5, '2025-02-25 13:43:51.000000'),
(6, 16, 7, 6.3, '2025-02-25 17:54:42.000000');

-- --------------------------------------------------------

--
-- Table structure for table `platos`
--

CREATE TABLE `platos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio` double NOT NULL,
  `img` varchar(255) NOT NULL,
  `destacado` tinyint(1) DEFAULT NULL,
  `oferta` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `platos`
--

INSERT INTO `platos` (`id`, `nombre`, `descripcion`, `precio`, `img`, `destacado`, `oferta`) VALUES
(1, 'Lasaña', 'Increible lasaña del copon, ta to wena la verda', 12.9, 'https://www.elespectador.com/resizer/v2/46JYAOVG3FBHVKVV2GIXNWYK2Y.jpg?auth=26c21126408a83ae7f66f0430a43463d65ca8857e808a445b099e9efb4fc8da0&width=920&height=613&smart=true&quality=60', 1, NULL),
(2, 'Fabada asturiana', 'Mejor no vayas al gimnasio jaja', 14.9, 'https://imag.bonviveur.com/fabada-asturiana.jpg', 0, NULL),
(3, 'Flamenquín con patatas', 'Fo k weno', 11.9, 'https://imag.bonviveur.com/flamenquines.jpg', 1, NULL),
(5, 'Pizza margarita', 'Echale algo no? xD', 10, 'https://imag.bonviveur.com/pizza-margarita.jpg', 0, NULL),
(6, 'Salmorejo', 'como entra en verano freskito k weno', 11.5, 'https://imag.bonviveur.com/salmorejo-cordobes.jpg', 1, NULL),
(7, 'Spanish Butifarra', 'mae mía como esta la salchicha', 6.3, 'https://www.casanoguera.com/wp-content/uploads/2024/03/como-cocinar-butifarra.jpg', 1, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `cocineros`
--
ALTER TABLE `cocineros`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cocineros_platos`
--
ALTER TABLE `cocineros_platos`
  ADD PRIMARY KEY (`cocinero_id`,`plato_id`),
  ADD KEY `plato_id` (`plato_id`);

--
-- Indexes for table `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `usuario_id` (`cliente_id`),
  ADD KEY `plato_id` (`plato_id`);

--
-- Indexes for table `platos`
--
ALTER TABLE `platos`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `cocineros`
--
ALTER TABLE `cocineros`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `platos`
--
ALTER TABLE `platos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cocineros_platos`
--
ALTER TABLE `cocineros_platos`
  ADD CONSTRAINT `cocineros_platos_ibfk_1` FOREIGN KEY (`cocinero_id`) REFERENCES `cocineros` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `cocineros_platos_ibfk_2` FOREIGN KEY (`plato_id`) REFERENCES `platos` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `pedidos_ibfk_2` FOREIGN KEY (`plato_id`) REFERENCES `platos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
