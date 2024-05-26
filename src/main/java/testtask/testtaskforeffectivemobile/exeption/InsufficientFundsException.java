package testtask.testtaskforeffectivemobile.exeption;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long fromBankAccount) {
        super("The transfer cannot be completed because there are not " +
            "enough funds on the account with ID " + fromBankAccount);
    }
}
