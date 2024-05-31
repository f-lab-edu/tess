package com.flab.tess.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.tess.domain.Account;
import com.flab.tess.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTest {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    protected User testUser;
    protected Principal mockPrincipal;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(initializeController())
                .apply(documentationConfiguration(provider))
                .build();
        testUser = createUser();
        mockPrincipal = new Principal() {
            @Override
            public String getName() {
                return "testUser";
            }
        };
    }

    User createUser() {

        User testUser = User.builder()
                .userId(BigInteger.valueOf(1))
                .loginId("testLoginId")
                .name("유정")
                .build();


        Account yuAccount = Account.builder()
                .accountId(BigInteger.valueOf(11))
                .accountNum("123456")
                .accountName("유정 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(25000))
                .user(testUser)
                .build();

        Account luckyAccount = Account.builder()
                .accountId(BigInteger.valueOf(12)) // 다른 값으로 변경
                .accountNum("7777")
                .accountName("행운의 계좌")
                .accountType("입출금")
                .balance(BigDecimal.valueOf(7777))
                .user(testUser)
                .build();

        User spyUser = spy(testUser);
        doReturn(List.of(yuAccount, luckyAccount)).when(spyUser).getAccountList();

        return spyUser;
    }


    protected abstract Object initializeController();
}
