package com.scout24.htmlanalysis.util;

public enum HtmlAnalysisConstants {

	HTML5("Html5"), PUBLIC_ID("publicid"), EMPTY_STRING(""), H1("h1"), H2("h2"), H3("h3"), H4("h4"), H5("h5"), H6(
			"h6"), HYPERMEDIA_TAG("[src]"), SCRIPT("script"), HTTP("http"), SRC("src"), EXTERNAL("external"), INTERNAL(
					"internal"), LINK_TAG("a[href]"), HREF("href"), QUERY_PER_THREAD(10), FORM(
							"form"), HTML_TEXT_TAG("input[type=text]"), HTML_PASSWORD_TAG(
									"input[type=password]"), HTML_SUBMIT_TAG("input[type=submit]");

	private HtmlAnalysisConstants(String value) {
		this.value = value;
	}

	private HtmlAnalysisConstants(int numericValue) {
		this.numericValue = numericValue;
	}

	private String value;

	private int numericValue;

	public String getValue() {
		return this.value;
	}

	public int getNumericValue() {
		return this.numericValue;
	}

}
