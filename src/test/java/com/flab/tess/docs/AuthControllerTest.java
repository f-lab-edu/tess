package com.flab.tess.docs;

import com.flab.tess.controller.AuthController;
import com.flab.tess.domain.User;
import com.flab.tess.dto.JoinRequest;
import com.flab.tess.dto.LoginRequest;
import com.flab.tess.dto.UserDto;
import com.flab.tess.provider.JwtTokenProvider;
import com.flab.tess.service.CustomUserDetailService;
import com.flab.tess.service.UserService;
import com.mysql.cj.log.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.MediaType;

import java.math.BigInteger;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends RestDocsTest{

    private final UserService userService = mock(UserService.class);
    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    private final CustomUserDetailService customUserDetailService = mock(CustomUserDetailService.class);

    @Override
    protected Object initializeController() {
        return new AuthController(userService, jwtTokenProvider, customUserDetailService);
    }

    @Test
    void 회원가입() throws Exception{

        //given
        JoinRequest joinRequestTest = new JoinRequest("유정", "testLoginId", "1234");
        given(userService.join(any(JoinRequest.class))).willReturn(testUser);

        // When & Then
        mockMvc.perform(post("/auth/join")
                .content(objectMapper.writeValueAsString(joinRequestTest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("userId").description("유저 id").optional(),
                                fieldWithPath("loginId").description("유저 loginId"),
                                fieldWithPath("userName").description("사용자 이름")
                        )
                ));
    }

    @Test
    void 유저_정보_조회() throws Exception{

        given(customUserDetailService.findUser(mockPrincipal)).willReturn(testUser);
        given(userService.getUserInfo(testUser.getUserId())).willReturn(testUser);

        //when & then
        this.mockMvc.perform(
                get("/auth/info").principal(mockPrincipal))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(status().isOk())
        //문서화
                .andDo(document("info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("userId").description("유저 id").optional(),
                                fieldWithPath("loginId").description("유저 loginId"),
                                fieldWithPath("userName").description("사용자 이름")
                        )
                ));

    }

    @Test
    void 로그인() throws Exception{

        LoginRequest loginRequestTest = new LoginRequest("testLoginId", "1234");
        given(userService.login(any(LoginRequest.class))).willReturn(testUser);

        // When & Then
        mockMvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(loginRequestTest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accessToken").description("토큰"),
                                fieldWithPath("userId").description("유저 ID")
                        )

                ));


    }

}
