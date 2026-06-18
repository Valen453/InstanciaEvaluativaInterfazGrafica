-- ============================================================
-- Script de creación de la base de datos: autogestion_db
-- Sistema de Autogestión Estudiantil - 2da Instancia Evaluativa
-- Bonus: DAO de base de datos (JDBC) para la entidad Estudiante
-- ============================================================

CREATE DATABASE IF NOT EXISTS autogestion_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE autogestion_db;

-- Tabla de estudiantes (persistida vía JDBC con EstudianteDAOJDBC)
CREATE TABLE IF NOT EXISTS estudiantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    legajo VARCHAR(20) NOT NULL UNIQUE,
    carrera VARCHAR(100) NOT NULL,
    anio_ingreso INT NOT NULL
);

-- Datos de ejemplo para poder probar el login de autogestión
-- (podés borrar estas filas y crear tu propio estudiante desde la app)
INSERT INTO estudiantes (nombre, legajo, carrera, anio_ingreso)
VALUES
    ('Ana Pérez', 'LEG001', 'Ingeniería en Sistemas', 2023),
    ('Juan Gómez', 'LEG002', 'Licenciatura en Informática', 2022)
ON DUPLICATE KEY UPDATE nombre = nombre;

-- Nota: Materia e InscripcionMateria siguen persistiéndose en archivos de
-- texto plano (materias.txt, inscripciones_<legajo>.txt) según lo pedido
-- en la consigna obligatoria. Solo Estudiante usa JDBC, como DAO adicional
-- del ítem BONUS, sin modificar Modelo, Controlador ni Vista.
