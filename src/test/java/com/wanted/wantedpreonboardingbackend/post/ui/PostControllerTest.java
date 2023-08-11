package com.wanted.wantedpreonboardingbackend.post.ui;


import static com.wanted.wantedpreonboardingbackend.auth.ui.AuthControllerTest.로그인_요청;
import static com.wanted.wantedpreonboardingbackend.member.ui.MemberControllerTest.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.wantedpreonboardingbackend.ControllerTest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostRequest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.service.PostService;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class PostControllerTest extends ControllerTest {

    private String token;

    @Autowired
    private PostService postService;

    @BeforeEach
    public void setUp() {
        super.setUp();

        final String EMAIL = "test@abc.com";
        final String PASSWORD = "12345678";

        회원_생성_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        token = response.jsonPath().getString("accessToken");
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

    @Test
    @DisplayName("글 목록을 조회할 수 있다.")
    void findPosts() {
        //given
        IntStream.range(1, 11)
                .mapToObj(i -> PostRequest.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .build())
                .forEach(postRequest -> postService.createPost(postRequest, 1L));

        //when
        ExtractableResponse<Response> response = 글_목록_조회_요청(token);

        //then
        응답결과_확인(response, HttpStatus.OK);
        List<PostResponse> postResponses = Arrays.asList(response.body().as(PostResponse[].class));
        assertThat(postResponses).hasSize(5);
        assertThat(postResponses.get(0).getId()).isEqualTo(1L);
        assertThat(postResponses.get(0).getTitle()).isEqualTo("제목1");
        assertThat(postResponses.get(0).getContent()).isEqualTo("내용1");
    }

    @Test
    @DisplayName("id로 글을 조회할 수 있다.")
    void findPost() {
        //given
        PostRequest post = PostRequest.builder()
                .title("제목")
                .content("내용")
                .build();
        PostResponse createdPostResponse = postService.createPost(post, 1L);

        //when
        ExtractableResponse<Response> response = 글_조회_요청(token, createdPostResponse.getId());

        //then
        응답결과_확인(response, HttpStatus.OK);
        PostResponse postResponse = response.body().as(PostResponse.class);
        assertThat(postResponse.getId()).isEqualTo(1L);
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

    public static ExtractableResponse<Response> 글_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when().get("/posts?page=0&size=5")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 글_조회_요청(String token, Long postId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when().get("/post/" + postId)
                .then().log().all()
                .extract();
    }
}
