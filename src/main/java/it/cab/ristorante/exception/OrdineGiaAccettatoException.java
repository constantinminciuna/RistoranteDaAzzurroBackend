package it.cab.ristorante.exception;

public class OrdineGiaAccettatoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrdineGiaAccettatoException(Long idOrdine) {
        super("L'ordine con ID " + idOrdine + " è già stato accettato.");
    }
	
}
