package testtask.testtaskforeffectivemobile.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import testtask.testtaskforeffectivemobile.dto.user.UserParamsDTO;
import testtask.testtaskforeffectivemobile.model.User;

import java.time.LocalDate;

@Component
public class UserSpecification {
    public Specification<User> build(UserParamsDTO params) {
        return withPhoneNumberAll(params.getPhoneNumberAll())
            .and(withFirstNameCont(params.getFirstNameCont()))
            .and(withLastNameCont(params.getLastNameCont()))
            .and(withSurnameCont(params.getSurnameCont()))
            .and(withEmailAll(params.getEmailAll()))
            .and(withBirthDataGt(params.getBirthDateGt()));
    }

    private Specification<User> withPhoneNumberAll(String phoneNumberAll) {
        return (root, query, cb) -> phoneNumberAll == null ? cb.conjunction()
            : cb.equal(root.get("phoneNumber").get("phoneNumber"), phoneNumberAll);
    }

    private Specification<User> withFirstNameCont(String firstNameCont) {
        return (root, query, cb) -> firstNameCont == null ? cb.conjunction()
            : cb.like((root.get("firstName")), "%" + firstNameCont + "%");
    }
    private Specification<User> withLastNameCont(String lastNameCont) {
        return (root, query, cb) -> lastNameCont == null ? cb.conjunction()
            : cb.like((root.get("lastName")), "%" + lastNameCont + "%");
    }
    private Specification<User> withSurnameCont(String surnameCont) {
        return (root, query, cb) -> surnameCont == null ? cb.conjunction()
            : cb.like((root.get("surname")), "%" + surnameCont + "%");
    }
    private Specification<User> withEmailAll(String emailAll) {
        return (root, query, cb) -> emailAll == null ? cb.conjunction()
            : cb.equal(root.get("email").get("email"), emailAll);
    }
    private Specification<User> withBirthDataGt(LocalDate birthDateGt) {
        return (root, query, cb) -> birthDateGt == null ? cb.conjunction()
            : cb.greaterThan(root.get("birthDate"), birthDateGt);
    }
}
