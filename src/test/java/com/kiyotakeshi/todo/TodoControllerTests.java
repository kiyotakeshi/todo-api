package com.kiyotakeshi.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class TodoControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private RestDocumentationContextProvider restDocumentationContextProvider;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(this.restDocumentationContextProvider).uris().withScheme("http")
						// .withHost("example.com")
						.withPort(8081))
				.build();
	}

	@Test
	void shouldReturnTodoList() throws Exception {
		this.mockMvc.perform(get("/todo").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andDo(
						// target/generated-snippets/todo
						document("getTodoList",
								responseFields(fieldWithPath("[].id").description("uniqe todo id"),
										(fieldWithPath("[].activityName").description("activity name")),
										(fieldWithPath("[].color").description("color")),
										(fieldWithPath("[].category").description("category")))));
	}

	@Test
	void shouldReturnTodo() throws Exception {
		this.mockMvc.perform(get("/todo/1").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(
						"{\"id\":1,\"activityName\":\"wash dishes\",\"color\":\"white\",\"category\":\"housework\"}"))
				.andDo(document("getTodo",
						responseFields(fieldWithPath("id").description("uniqe todo id"),
								(fieldWithPath("activityName").description("activity name")),
								(fieldWithPath("color").description("color")),
								(fieldWithPath("category").description("category")))));
	}

	@Test
	void shouldCreateTodo() throws Exception {
		this.mockMvc.perform(post("/todo/") //
				.param("activityName", "test") //
				.param("color", "black") //
				.param("category", "test")) //
				.andExpect(status().isCreated()) //
				.andExpect(content()
						.json("{\"id\":6,\"activityName\":\"test\",\"color\":\"black\",\"category\":\"test\"}"))
				.andDo(document("postTodo"));
	}

	@Test
	void shouldUpdateTodo() throws Exception {
		this.mockMvc.perform(put("/todo/5") //
				.param("activityName", "update") //
				.param("color", "red") //
				.param("category", "update")) //
				.andExpect(status().isOk()) //
                .andExpect(content()
                .json("{\"id\":5,\"activityName\":\"update\",\"color\":\"red\",\"category\":\"update\"}"))
                .andDo(document("putTodo"));
	}

}