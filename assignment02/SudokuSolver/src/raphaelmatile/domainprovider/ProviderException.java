package raphaelmatile.domainprovider;

/**
 * Thrown to indicate that an error occurred during providing a value.
 */
class ProviderException extends RuntimeException {

    /**
     * Constructs a new exception with the provided detail message
     *
     * @param message A message indicating why this exception was thrown
     */
    ProviderException(String message) {
        super(message);
    }
}
