package com.wanted.wantedpreonboardingbackend.post.ui;


import static com.wanted.wantedpreonboardingbackend.auth.ui.AuthControllerTest.로그인_요청;
import static com.wanted.wantedpreonboardingbackend.member.ui.MemberControllerTest.회원_생성을_요청;

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

class PostControllerTest extends ControllerTest {

    private String token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        final String EMAIL = "test@abc.com";
        final String PASSWORD = "12345678";

        회원_생성을_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        token = response.jsonPath().getString("accessToken");
        System.out.println("### token = " + token);
    }

    @Test
    @DisplayName("글을 등록할 수 있다.")
    void createPost() {
        //when
        ExtractableResponse<Response> response = 글_생성_요청(token, "제목", "내용");

        //then
        응답결과_확인(response, HttpStatus.CREATED);
    }

    @Test
    @DisplayName("제목 없이 글을 등록할 수 없다.")
    void emptyTitle_createPost() {
        //when
        ExtractableResponse<Response> response = 글_생성_요청(token, null, "내용");

        //then
        응답결과_확인(response, HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("내용 없이 글을 등록할 수 없다.")
    void emptyContent_createPost() {
        //when
        ExtractableResponse<Response> response = 글_생성_요청(token, "제목", null);

        //then
        응답결과_확인(response, HttpStatus.BAD_REQUEST);
    }

    public static ExtractableResponse<Response> 글_생성_요청(String token, String title, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(params)
                .when().post("/post")
                .then().log().all()
                .extract();
    }
}
