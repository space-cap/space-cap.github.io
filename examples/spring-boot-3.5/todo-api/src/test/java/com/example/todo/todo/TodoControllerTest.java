package com.example.todo.todo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TodoService todoService;

    @Test
    void createsTodo() throws Exception {
        TodoResponse response = new TodoResponse(
                1L,
                "Spring Boot 공부하기",
                "예제 프로젝트 만들기",
                false,
                LocalDateTime.of(2026, 5, 19, 12, 0)
        );
        given(todoService.create(any(CreateTodoRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Spring Boot 공부하기",
                                  "description": "예제 프로젝트 만들기"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/todos/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot 공부하기"));
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "description": "제목은 필수입니다."
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)));
    }

    @Test
    void findsTodos() throws Exception {
        given(todoService.findAll()).willReturn(List.of(
                new TodoResponse(1L, "첫 번째 할 일", null, false, LocalDateTime.now()),
                new TodoResponse(2L, "두 번째 할 일", null, true, LocalDateTime.now())
        ));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
