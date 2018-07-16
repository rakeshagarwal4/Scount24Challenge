package com.scout24.htmlanalysis.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.scout24.htmlanalysis.dto.Response;
import com.scout24.htmlanalysis.service.HtmlAnalysisService;

@RunWith(MockitoJUnitRunner.class)
public class HtmlAnalysisControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private HtmlAnalysisController htmlAnalysisController;

	@Mock
	private HtmlAnalysisService htmlAnalysisService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(htmlAnalysisController).build();
	}

	@Test
	public void testHtmlAnalysisException() throws Exception {
		Mockito.doThrow(IOException.class).when(htmlAnalysisService).getHtmlAnalysisInfo(Mockito.anyString());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/scout24/htmlanalysis").param("url",
				"http://test.com");
		mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
	}

	@Test
	public void testHtmlAnalysis() throws Exception {
		Response response = Response.builder().pageTitle("Test Page").loginForm(true).build();
		Mockito.when(htmlAnalysisService.getHtmlAnalysisInfo(Mockito.anyString())).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/scout24/htmlanalysis").param("url",
				"http://www.google.com");
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse servletResponse = mvcResult.getResponse();
		assertEquals(HttpStatus.OK.value(), servletResponse.getStatus());
	}

}
