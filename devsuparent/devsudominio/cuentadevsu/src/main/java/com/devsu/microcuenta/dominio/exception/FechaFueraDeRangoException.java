package com.devsu.microcuenta.dominio.exception;

public class FechaFueraDeRangoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2878769323988132480L;

	public FechaFueraDeRangoException(String message) {
        super(message);
    }
}
