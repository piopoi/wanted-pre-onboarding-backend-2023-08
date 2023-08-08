package com.wanted.wantedpreonboardingbackend.member.ui;

import com.wanted.wantedpreonboardingbackend.ControllerTest;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberControllerTest extends ControllerTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("사용자를 생성할 수 있다.")
    void createMember() {
        //given when
        ExtractableResponse<Response> response = 회원_생성을_요청("test@abc.com", "12345678");

        //then
        응답결과_확인(response, HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password) {
        MemberRequest memberRequest = MemberRequest.builder()
                .email(email)
                .password(password)
                .build();

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/member")
                .then().log().all()
                .extract();
    }
}
