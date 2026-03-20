package is.sio.jpa.services;

public class EcoleDejaCreeeException extends Exception {
	public EcoleDejaCreeeException(String message) {
		super(message);
	}

	public EcoleDejaCreeeException() {
		super("L'école existe déjà");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
