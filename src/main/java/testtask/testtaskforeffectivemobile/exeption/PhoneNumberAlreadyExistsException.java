package testtask.testtaskforeffectivemobile.exeption;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("PhoneNumber already exists: " + phoneNumber);
    }
}
