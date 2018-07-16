package com.scout24.htmlanalysis.logic;

import java.util.List;

import org.jsoup.nodes.Document;

import com.scout24.htmlanalysis.dto.HeadingLink;
import com.scout24.htmlanalysis.dto.LinkDetails;

public interface HtmlAnalysisLogic {

	/**
	 * Get the Html version of the document.
	 * 
	 * @param document
	 * @return String
	 */
	String getHtmlVersion(Document document);

	/**
	 * Get the Page title.
	 * 
	 * @param document
	 * @return String
	 */
	String getPageTitle(Document document);

	/**
	 * Get the List of headers in the Page. grouped by header level.
	 * 
	 * @param document
	 * @return List of Headers
	 */
	List<HeadingLink> getHeaders(Document document);

	/**
	 * Get the List of Hypermedia links in the Page. grouped by External and
	 * internal links.
	 * 
	 * @param document
	 * @return List of links
	 */
	List<HeadingLink> getHyperMediaLinks(Document document);

	/**
	 * Get the list of Link details in the page.
	 * 
	 * @param document
	 * @return List of Link details.
	 */
	List<LinkDetails> getLinkDetails(Document document);

	/**
	 * check if login form is present or not in the page.
	 * 
	 * @param document
	 * @return boolean
	 */
	boolean isLoginFormPresent(Document document);

}
