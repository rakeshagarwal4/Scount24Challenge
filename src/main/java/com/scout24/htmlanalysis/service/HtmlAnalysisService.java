package com.scout24.htmlanalysis.service;

import java.io.IOException;

import com.scout24.htmlanalysis.dto.Response;

public interface HtmlAnalysisService {

	/**
	 * Analyse the given Url and return the information of the html pag.
	 * 
	 * @param url
	 * @return Response
	 * @throws IOException
	 *             - If Url is incorrect
	 */
	public Response getHtmlAnalysisInfo(String url) throws IOException;

}
