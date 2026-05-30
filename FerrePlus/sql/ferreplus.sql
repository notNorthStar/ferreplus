-- ============================================================
-- Ferre+ - Script de creacion de la base de datos
-- Equipo 3: Sosa Fernandez Luis David / Gutierrez Colorado Oliver
-- Motor: MySQL 8
-- Autor: Sosa (tarea 2 del EDT)
-- ============================================================

DROP DATABASE IF EXISTS ferreplus;
CREATE DATABASE ferreplus
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE ferreplus;

-- ------------------------------------------------------------
-- Tabla: usuarios
-- ------------------------------------------------------------
CREATE TABLE usuarios (
    id              INT             NOT NULL AUTO_INCREMENT,
    username        VARCHAR(50)     NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    nombre_completo VARCHAR(100)    NOT NULL,
    activo          BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE KEY uq_usuarios_username (username)
) ENGINE = InnoDB;

-- ------------------------------------------------------------
-- Tabla: productos
-- La clave debe ser exactamente 9 digitos numericos (RF-03)
-- ------------------------------------------------------------
CREATE TABLE productos (
    clave     CHAR(9)        NOT NULL,
    nombre    VARCHAR(100)   NOT NULL,
    cantidad  INT            NOT NULL DEFAULT 0,
    ubicacion VARCHAR(50)    NOT NULL,
    precio    DECIMAL(10, 2) NOT NULL,
    foto      LONGBLOB       NULL,
    PRIMARY KEY (clave),
    CONSTRAINT chk_productos_clave CHECK (clave REGEXP '^[0-9]{9}$'),
    CONSTRAINT chk_productos_cantidad CHECK (cantidad >= 0),
    CONSTRAINT chk_productos_precio CHECK (precio >= 0)
) ENGINE = InnoDB;

-- ------------------------------------------------------------
-- Tabla: bitacora_eliminaciones (RF-05, RF-06, RNF-S03)
-- Solo INSERT desde la aplicacion
-- ------------------------------------------------------------
CREATE TABLE bitacora_eliminaciones (
    id              INT          NOT NULL AUTO_INCREMENT,
    clave_producto  CHAR(9)      NOT NULL,
    nombre_producto VARCHAR(100) NOT NULL,
    motivo          TEXT         NOT NULL,
    fecha           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario      INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_bitacora_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
) ENGINE = InnoDB;

-- ------------------------------------------------------------
-- Tabla: ventas (cabecera)
-- ------------------------------------------------------------
CREATE TABLE ventas (
    id         INT            NOT NULL AUTO_INCREMENT,
    fecha      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal   DECIMAL(10, 2) NOT NULL,
    impuestos  DECIMAL(10, 2) NOT NULL,
    total      DECIMAL(10, 2) NOT NULL,
    id_usuario INT            NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ventas_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
) ENGINE = InnoDB;

-- ------------------------------------------------------------
-- Tabla: detalle_venta (renglones del ticket)
-- ------------------------------------------------------------
CREATE TABLE detalle_venta (
    id              INT            NOT NULL AUTO_INCREMENT,
    id_venta        INT            NOT NULL,
    clave_producto  CHAR(9)        NOT NULL,
    cantidad        INT            NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal        DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_venta
        FOREIGN KEY (id_venta) REFERENCES ventas (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (clave_producto) REFERENCES productos (clave),
    CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0)
) ENGINE = InnoDB;

-- ============================================================
-- Datos semilla
-- ============================================================

-- Usuario administrador para poder iniciar sesion desde el dia 1
-- usuario: admin   contrasena: admin123
INSERT INTO usuarios (username, password, nombre_completo, activo)
VALUES ('admin', 'admin123', 'Administrador Ferre+', TRUE);

-- Un par de productos de ejemplo para probar el modulo de inventario
INSERT INTO productos (clave, nombre, cantidad, ubicacion, precio) VALUES
('100000001', 'Martillo de bola 16 oz',     25, 'Estante A-1', 189.50),
('100000002', 'Desarmador plano 6 pulgadas', 40, 'Estante A-2',  59.90),
('100000003', 'Caja de clavos 2 pulgadas',  100, 'Estante B-1',  35.00);
