package com.wanted.wantedpreonboardingbackend.post.ui;


import static com.wanted.wantedpreonboardingbackend.auth.ui.AuthControllerTest.로그인_요청;
import static com.wanted.wantedpreonboardingbackend.member.ui.MemberControllerTest.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.wanted.wantedpreonboardingbackend.ControllerTest;
import com.wanted.wantedpreonboardingbackend.post.application.PostService;
import com.wanted.wantedpreonboardingbackend.post.dto.PostRequest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostResponse;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.FieldDescriptor;

class PostControllerTest extends ControllerTest {

    private final Long createdPostId = 1L;

    private String token;

    @Autowired
    private PostService postService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

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
                .forEach(postRequest -> postService.createPost(postRequest, createdPostId));

        //when
        ExtractableResponse<Response> response = 글_목록_조회_요청(token);

        //then
        응답결과_확인(response, HttpStatus.OK);
        List<PostResponse> postResponses = Arrays.asList(response.body().as(PostResponse[].class));
        assertThat(postResponses).hasSize(5);
        assertThat(postResponses.get(0).getId()).isEqualTo(createdPostId);
        assertThat(postResponses.get(0).getTitle()).isEqualTo("제목1");
        assertThat(postResponses.get(0).getContent()).isEqualTo("내용1");
    }

    @Test
    @DisplayName("id로 글을 조회할 수 있다.")
    void findPost() {
        //given
        글_생성_요청(token, "제목", "내용");

        //when
        ExtractableResponse<Response> response = 글_조회_요청(token, createdPostId);

        //then
        응답결과_확인(response, HttpStatus.OK);
        PostResponse postResponse = response.body().as(PostResponse.class);
        assertThat(postResponse.getId()).isEqualTo(createdPostId);
    }

    @Test
    @DisplayName("작성자는 id로 글을 수정할 수 있다.")
    void updatePost() {
        //given
        글_생성_요청(token, "제목", "내용");

        //when
        ExtractableResponse<Response> updateResponse = 글_수정_요청(token, createdPostId, "수정된 제목", "수정된 내용");

        //then
        응답결과_확인(updateResponse, HttpStatus.OK);
    }

    @Test
    @DisplayName("작성자는 id로 글을 삭제할 수 있다.")
    void deletePost() {
        //given
        글_생성_요청(token, "제목", "내용");

        //when
        ExtractableResponse<Response> response = 글_삭제_요청(token, createdPostId);

        //then
        응답결과_확인(response, HttpStatus.NO_CONTENT);
    }

    public static ExtractableResponse<Response> 글_생성_요청(String token, String title, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);

        return RestAssured
                .given(spec).log().all()
                .filter(document("post/create-post",
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(params)
                .when().post("/post")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 글_목록_조회_요청(String token) {
        FieldDescriptor[] postFieldDescriptors = new FieldDescriptor[]{
                fieldWithPath("id").description("게시글 아이디"),
                fieldWithPath("title").description("게시글 제목"),
                fieldWithPath("content").description("게시글 내용"),
                fieldWithPath("memberId").description("작성자 아이디")
        };

        return RestAssured
                .given(spec).log().all()
                .filter(document("post/find-post-list",
                        responseFields(fieldWithPath("[]").description("게시글 목록"))
                                .andWithPrefix("[].", postFieldDescriptors)
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when().get("/posts")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 글_조회_요청(String token, Long postId) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("post/find-post",
                        pathParameters(
                                parameterWithName("id").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 아이디"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("memberId").description("작성자 아이")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when().get("/post/{id}", postId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 글_수정_요청(String token,
                                                        Long postId,
                                                        String title,
                                                        String content) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);

        return RestAssured
                .given(spec).log().all()
                .filter(document("post/update-post",
                        pathParameters(
                                parameterWithName("id").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(params)
                .when().put("/post/{id}", postId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 글_삭제_요청(String token, Long postId) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("post/delete-post",
                        pathParameters(
                                parameterWithName("id").description("게시글 아이디")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .when().delete("/post/{id}", postId)
                .then().log().all()
                .extract();
    }
}
