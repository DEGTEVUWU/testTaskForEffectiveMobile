package testtask.testtaskforeffectivemobile.exeption;

public class LastPhoneNumberContactException extends RuntimeException {
    public LastPhoneNumberContactException(Long phoneNumberId) {
        super("You cannot delete an phoneNumber with Id " + phoneNumberId + " because it is the last one for this user");
    }
}
