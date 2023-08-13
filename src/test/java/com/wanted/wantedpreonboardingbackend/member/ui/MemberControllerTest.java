package com.wanted.wantedpreonboardingbackend.member.ui;

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

public class MemberControllerTest extends ControllerTest {

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
    }

    @Test
    @DisplayName("사용자를 생성할 수 있다.")
    void createMember() {
        //given when
        ExtractableResponse<Response> response = 회원_생성_요청("test@abc.com", "12345678");

        //then
        응답결과_확인(response, HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
                .given(spec).log().all()
                .filter(document("member/create-member",
                        requestFields(
                                fieldWithPath("email").description("사용자 아이디"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/member")
                .then().log().all()
                .extract();
    }
}
