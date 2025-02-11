-- Inserción de personas (clientes)
INSERT INTO Persona (nombre, edad, genero, identificacion, direccion, telefono)
VALUES
    ('Cliente 1', 30, 'M', '123456789', 'Calle 123', '1234567890'),
    ('Cliente 2', 25, 'F', '987654321', 'Av. Principal', '9876543210'),
    ('Cliente 3', 35, 'M', '456789123', 'Carrera 45', '4567891230');
-- Inserción de clientes
INSERT INTO Cliente (cliente_id, contrasena, estado)
VALUES
    (1, 'contraseña1', 'activo'),
    (2, 'contraseña2', 'activo'),
    (3, 'contraseña3', 'activo');

-- Inserción de cuentas
INSERT INTO Cuenta (numeroCuenta, tipoCuenta, saldoInicial, estado, cliente_id)
VALUES
    ('CUENTA001', 'AHORROS', 1000.00, 'ACTIVA', 1),  -- Cliente 1
    ('CUENTA002', 'CORRIENTE', 5000.00, 'ACTIVA', 1),  -- Cliente 1
    ('CUENTA003', 'AHORROS', 2000.00, 'ACTIVA', 2),  -- Cliente 2
    ('CUENTA004', 'CORRIENTE', 7000.00, 'ACTIVA', 2),  -- Cliente 2
    ('CUENTA005', 'AHORROS', 1500.00, 'ACTIVA', 3);  -- Cliente 3

-- Inserción de movimientos (5 movimientos por cada cuenta)
INSERT INTO Movimiento (fecha, tipoMovimiento, valor, saldo, cuenta_id)
VALUES
    (CURRENT_TIMESTAMP, 'DEBITO', 100.00, 900.00, 1),   -- Movimientos para CUENTA001
    (CURRENT_TIMESTAMP, 'CREDITO', 200.00, 1100.00, 1),
    (CURRENT_TIMESTAMP, 'DEBITO', 50.00, 1050.00, 1),
    (CURRENT_TIMESTAMP, 'CREDITO', 300.00, 1350.00, 1),
    (CURRENT_TIMESTAMP, 'DEBITO', 70.00, 1280.00, 1),
    (CURRENT_TIMESTAMP, 'DEBITO', 150.00, 4850.00, 2),  -- Movimientos para CUENTA002
    (CURRENT_TIMESTAMP, 'CREDITO', 500.00, 5350.00, 2),
    (CURRENT_TIMESTAMP, 'DEBITO', 200.00, 5150.00, 2),
    (CURRENT_TIMESTAMP, 'CREDITO', 100.00, 5250.00, 2),
    (CURRENT_TIMESTAMP, 'DEBITO', 80.00, 5170.00, 2),
    (CURRENT_TIMESTAMP, 'DEBITO', 50.00, 1950.00, 3),   -- Movimientos para CUENTA003
    (CURRENT_TIMESTAMP, 'CREDITO', 300.00, 2250.00, 3),
    (CURRENT_TIMESTAMP, 'DEBITO', 25.00, 2225.00, 3),
    (CURRENT_TIMESTAMP, 'CREDITO', 150.00, 2375.00, 3),
    (CURRENT_TIMESTAMP, 'DEBITO', 70.00, 2305.00, 3),
    (CURRENT_TIMESTAMP, 'DEBITO', 120.00, 6880.00, 4),  -- Movimientos para CUENTA004
    (CURRENT_TIMESTAMP, 'CREDITO', 700.00, 7580.00, 4),
    (CURRENT_TIMESTAMP, 'DEBITO', 300.00, 7280.00, 4),
    (CURRENT_TIMESTAMP, 'CREDITO', 250.00, 7530.00, 4),
    (CURRENT_TIMESTAMP, 'DEBITO', 150.00, 7380.00, 4),
    (CURRENT_TIMESTAMP, 'DEBITO', 80.00, 1420.00, 5),   -- Movimientos para CUENTA005
    (CURRENT_TIMESTAMP, 'CREDITO', 400.00, 1820.00, 5),
    (CURRENT_TIMESTAMP, 'DEBITO', 50.00, 1770.00, 5),
    (CURRENT_TIMESTAMP, 'CREDITO', 200.00, 1970.00, 5),
    (CURRENT_TIMESTAMP, 'DEBITO', 70.00, 1900.00, 5);
