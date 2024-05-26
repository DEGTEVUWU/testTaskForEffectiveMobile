package testtask.testtaskforeffectivemobile.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import testtask.testtaskforeffectivemobile.dto.TransferRequestDTO;
import testtask.testtaskforeffectivemobile.model.BankAccount;
import testtask.testtaskforeffectivemobile.model.User;
import testtask.testtaskforeffectivemobile.repository.BankAccountRepository;
import testtask.testtaskforeffectivemobile.repository.EmailRepository;
import testtask.testtaskforeffectivemobile.repository.PhoneNumberRepository;
import testtask.testtaskforeffectivemobile.repository.UserRepository;
import testtask.testtaskforeffectivemobile.utils.CreateTestUser;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private CreateTestUser createTestUser;

    private User testUser1, testUser2;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testUser1 = createTestUser.createTestUser();
        testUser2 = createTestUser.createTestUser();
        token = jwt().jwt(builder -> builder.subject(testUser1.getLogin())); //токен для выполнения  операций над
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
        bankAccountRepository.deleteAll();
        phoneNumberRepository.deleteAll();
        emailRepository.deleteAll();
    }

    @Test
    public void transferMoney() throws Exception {
        BigDecimal oldValueFromAccount = testUser1.getBankAccount().getBalance();
        BigDecimal oldValueToAccount = testUser2.getBankAccount().getBalance();
        BigDecimal amountToSubtract = new BigDecimal("100"); // Преобразуем сумму вычитания в BigDecimal.

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setFromAccountId(testUser1.getBankAccount().getId());
        transferRequestDTO.setToAccountId(testUser2.getBankAccount().getId());
        transferRequestDTO.setAmount(amountToSubtract);

        MockHttpServletRequestBuilder request = post("/api/users/{id}/bankAccount/transfer",
            testUser1.getId()).with(token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transferRequestDTO));

        mockMvc.perform(request)
            .andExpect(status().isOk());

        BankAccount updatedFromAccount = bankAccountRepository.findById(
            testUser1.getBankAccount().getId()).get();
        BankAccount updatedToAccount = bankAccountRepository.findById(
            testUser2.getBankAccount().getId()).get();

        assertEquals(0, updatedFromAccount.getBalance()
            .compareTo(oldValueFromAccount.subtract(amountToSubtract)));
        assertEquals(0, updatedToAccount.getBalance()
            .compareTo(oldValueToAccount.add(amountToSubtract)));
    }

}
