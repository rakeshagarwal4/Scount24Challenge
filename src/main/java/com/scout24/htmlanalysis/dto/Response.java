package com.scout24.htmlanalysis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Response {
	
	private String htmlVersion;
	private String pageTitle;
	private boolean loginForm;
	private List<HeadingLink> headings;
	private List<HeadingLink> links;
	private List<LinkDetails> linkDetails;

}
