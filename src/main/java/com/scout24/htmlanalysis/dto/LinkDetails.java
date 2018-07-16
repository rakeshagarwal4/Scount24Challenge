package com.scout24.htmlanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class LinkDetails {
	private String linkUrl;
	private boolean isLinkReachable;
	private String error;
	
}
