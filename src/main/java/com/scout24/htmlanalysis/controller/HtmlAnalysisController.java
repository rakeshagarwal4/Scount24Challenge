package com.scout24.htmlanalysis.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scout24.htmlanalysis.dto.Response;
import com.scout24.htmlanalysis.service.HtmlAnalysisService;

/**
 * Controller for Html Analysis.
 * 
 * @author rakeshagarwal
 *
 */
@RequestMapping("/v1/scout24/")
@RestController
public class HtmlAnalysisController {

	@Autowired
	private HtmlAnalysisService htmlAnalysisService;

	/**
	 * Exposed method which accepts url and analyse that url.
	 * 
	 * @param url
	 *            - Url to be analysed
	 * @return - Response
	 */
	@GetMapping("htmlanalysis")
	public ResponseEntity<Response> htmlAnalysis(@RequestParam String url) {
		Response response;
		try {
			response = htmlAnalysisService.getHtmlAnalysisInfo(url);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.<Response> ok(response);
	}
}
