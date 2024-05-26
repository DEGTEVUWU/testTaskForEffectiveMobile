package testtask.testtaskforeffectivemobile.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import testtask.testtaskforeffectivemobile.dto.phoneNumber.PhoneNumberDTO;
import testtask.testtaskforeffectivemobile.service.PhoneNumberService;

import java.util.List;

@RestController
@RequestMapping("/api/phone-number")
@AllArgsConstructor
@Slf4j
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PhoneNumberDTO>> index() {
        List<PhoneNumberDTO> phoneNumbers = phoneNumberService.getAll();
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(phoneNumbers.size()))
            .body(phoneNumbers);
    }
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PhoneNumberDTO> show(@PathVariable Long id) {
        PhoneNumberDTO phoneNumber = phoneNumberService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(phoneNumber);
    }
}
