package testtask.testtaskforeffectivemobile.exeption;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String login) {
        super("Login already exists: " + login);
    }
}
