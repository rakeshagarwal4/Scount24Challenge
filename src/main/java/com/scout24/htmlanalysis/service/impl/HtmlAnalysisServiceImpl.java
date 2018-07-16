package com.scout24.htmlanalysis.service.impl;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scout24.htmlanalysis.dto.HeadingLink;
import com.scout24.htmlanalysis.dto.LinkDetails;
import com.scout24.htmlanalysis.dto.Response;
import com.scout24.htmlanalysis.logic.HtmlAnalysisLogic;
import com.scout24.htmlanalysis.service.HtmlAnalysisService;

@Service
public class HtmlAnalysisServiceImpl implements HtmlAnalysisService {

	@Autowired
	private HtmlAnalysisLogic htmlAnalysisLogic;

	@Override
	public Response getHtmlAnalysisInfo(String url) throws IOException {

		Document document = getDocument(url);
		String htmlVersion = htmlAnalysisLogic.getHtmlVersion(document);
		String pageTitle = htmlAnalysisLogic.getPageTitle(document);
		List<HeadingLink> headers = htmlAnalysisLogic.getHeaders(document);

		List<HeadingLink> hyperMediaLinks = htmlAnalysisLogic.getHyperMediaLinks(document);
		List<LinkDetails> linkDetails = htmlAnalysisLogic.getLinkDetails(document);
		boolean isLoginFormPresent = htmlAnalysisLogic.isLoginFormPresent(document);
		return Response.builder().htmlVersion(htmlVersion).pageTitle(pageTitle).loginForm(isLoginFormPresent)
				.headings(headers).links(hyperMediaLinks).linkDetails(linkDetails).build();
	}

	private Document getDocument(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

}
