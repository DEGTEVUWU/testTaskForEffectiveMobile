package testtask.testtaskforeffectivemobile.exeption;

public class LastEmailContactException extends RuntimeException {
    public LastEmailContactException(Long emailId) {
        super("You cannot delete an email with Id " + emailId + " because it is the last one for this user");
    }
}
