package it.cab.ristorante.exception;

public class OrdineNonAccettatoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrdineNonAccettatoException(Long idOrdine) {
		super("L'ordine con ID " + idOrdine + " non è stato accettato.");
	}
}
