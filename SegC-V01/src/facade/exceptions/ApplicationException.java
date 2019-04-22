package facade.exceptions;


/**
 * The top level application exception.
 */
public class ApplicationException extends Exception {

    /**
     * The serial version id (generated automatically by Eclipse)
     */
    private static final long serialVersionUID = -3416001628323171383L;
    
    private int code;


    /**
     * Creates an exception given an error message
     *
     * @param message The error message
     */
    public ApplicationException(String message) {
        super(message);
    }


    /**
     * Creates an exception wrapping a lower level exception.
     *
     * @param message The error message
     * @param e       The wrapped exception.
     */
    public ApplicationException(String message, Exception e) {
        super(message, e);
    }


	public ApplicationException(int code) {
		super();
		this.code = code;
	}


	public int getCode() {
		return code;
	}
	
	

}
