package com.wanted.wantedpreonboardingbackend.auth.ui;

import static com.wanted.wantedpreonboardingbackend.member.ui.MemberControllerTest.회원_생성_요청;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.wanted.wantedpreonboardingbackend.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

public class AuthControllerTest extends ControllerTest {

    public static final String EMAIL = "test@abc.com";
    public static final String PASSWORD = "12345678";

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
    }

    @Test
    @DisplayName("로그인 할 수 있다")
    public void login() {
        //given
        ExtractableResponse<Response> createdMemberResponse = 회원_생성_요청(EMAIL, PASSWORD);
        응답결과_확인(createdMemberResponse, HttpStatus.CREATED);

        //when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);

        //then
        응답결과_확인(loginResponse, HttpStatus.OK);
    }

    @Test
    @DisplayName("잘못된 형식의 이메일로 로그인 할 수 없다.")
    void invalidEmail_login() {
        //when
        ExtractableResponse<Response> loginResponse = 로그인_요청("test.wanted.com", PASSWORD);

        //then
        응답결과_확인(loginResponse, HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("잘못된 형식의 비밀번호로 로그인 할 수 없다.")
    void invalidPassword_login() {
        //when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, "01234");

        //then
        응답결과_확인(loginResponse, HttpStatus.BAD_REQUEST);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
                .given(spec).log().all()
                .filter(document("auth/login",
                        requestFields(
                                fieldWithPath("email").description("사용자 아이디"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract();
    }
}
