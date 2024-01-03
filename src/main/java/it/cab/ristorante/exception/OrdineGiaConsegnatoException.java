package it.cab.ristorante.exception;

public class OrdineGiaConsegnatoException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrdineGiaConsegnatoException(Long idOrdine) {
        super("L'ordine con ID " + idOrdine + " è già stato accettato.");
    }
	
}
