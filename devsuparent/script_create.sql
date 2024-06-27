-- Creación de tabla Persona
CREATE TABLE Persona (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    edad INTEGER NOT NULL,
    genero VARCHAR(1) NOT NULL,
    identificacion VARCHAR(30) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20)
);

-- Creación de tabla Cliente
CREATE TABLE Cliente (
    cliente_id BIGINT PRIMARY KEY,
    contrasena VARCHAR(255),
    estado VARCHAR(50),
    FOREIGN KEY (cliente_id) REFERENCES Persona(id)
);

-- Creación de tabla Cuenta
CREATE TABLE Cuenta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numeroCuenta VARCHAR(255) UNIQUE,
    tipoCuenta VARCHAR(20),
    saldoInicial DECIMAL(19, 2),
    estado VARCHAR(50),
    cliente_id BIGINT,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(cliente_id)
);

-- Creación de tabla Movimiento
CREATE TABLE Movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP,
    tipoMovimiento VARCHAR(20),
    valor DECIMAL(19, 2),
    saldo DECIMAL(19, 2),
    cuenta_id BIGINT,
    FOREIGN KEY (cuenta_id) REFERENCES Cuenta(id)
);