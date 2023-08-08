package com.wanted.wantedpreonboardingbackend.auth.ui;

import static com.wanted.wantedpreonboardingbackend.member.ui.MemberControllerTest.회원_생성을_요청;

import com.wanted.wantedpreonboardingbackend.ControllerTest;
import com.wanted.wantedpreonboardingbackend.auth.dto.TokenRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthControllerTest extends ControllerTest {

    public static final String EMAIL = "test@abc.com";
    public static final String PASSWORD = "12345678";

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("이메일로 로그인 할 수 있다")
    public void loginMember() {
        //given
        ExtractableResponse<Response> createdMemberResponse = 회원_생성을_요청(EMAIL, PASSWORD);
        응답결과_확인(createdMemberResponse, HttpStatus.CREATED);

        //when
        ExtractableResponse<Response> loginResponse = 이메일_로그인_요청(EMAIL, PASSWORD);

        //then
        응답결과_확인(loginResponse, HttpStatus.OK);
    }

    public static ExtractableResponse<Response> 이메일_로그인_요청(String email, String password) {
        TokenRequest tokenRequest = TokenRequest.builder()
                .email(email)
                .password(password)
                .build();

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
