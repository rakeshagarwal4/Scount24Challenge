package com.scout24.htmlanalysis.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.scout24.htmlanalysis.dto.HeadingLink;
import com.scout24.htmlanalysis.dto.LinkDetails;
import com.scout24.htmlanalysis.logic.HtmlAnalysisLogic;

import static com.scout24.htmlanalysis.util.HtmlAnalysisConstants.*;

@Component
public class HtmlAnalysisLogicImpl implements HtmlAnalysisLogic {

	@Override
	public String getHtmlVersion(Document document) {
		List<Node> nods = document.childNodes();
		String htmlVersion = HTML5.getValue();
		for (Node node : nods) {
			if (node instanceof DocumentType) {
				DocumentType documentType = (DocumentType) node;
				String publicId = documentType.attr(PUBLIC_ID.getValue());
				if (!EMPTY_STRING.getValue().equals(publicId)) {
					htmlVersion = publicId;
				}
			}
		}
		return htmlVersion;
	}

	@Override
	public String getPageTitle(Document document) {
		return document.title();
	}

	@Override
	public List<HeadingLink> getHeaders(Document document) {

		HeadingLink h1 = getHeadingLink(document, H1.getValue());
		HeadingLink h2 = getHeadingLink(document, H2.getValue());
		HeadingLink h3 = getHeadingLink(document, H3.getValue());
		HeadingLink h4 = getHeadingLink(document, H4.getValue());
		HeadingLink h5 = getHeadingLink(document, H5.getValue());
		HeadingLink h6 = getHeadingLink(document, H6.getValue());

		List<HeadingLink> headers = new LinkedList<>();
		if (h1 != null) {
			headers.add(h1);
		}
		if (h2 != null) {
			headers.add(h2);
		}
		if (h3 != null) {
			headers.add(h3);
		}
		if (h4 != null) {
			headers.add(h4);
		}
		if (h5 != null) {
			headers.add(h5);
		}
		if (h6 != null) {
			headers.add(h6);
		}
		return headers;
	}

	@Override
	public List<HeadingLink> getHyperMediaLinks(Document document) {

		Elements elements = document.select(HYPERMEDIA_TAG.getValue());
		List<Element> mediaLinkList = elements.stream().filter(t -> !t.tagName().equals(SCRIPT.getValue()))
				.collect(Collectors.toList());
		List<Element> externalLinks = mediaLinkList.stream()
				.filter(t -> t.attr(SRC.getValue()).startsWith(HTTP.getValue())).collect(Collectors.toList());
		mediaLinkList.removeAll(externalLinks);
		List<HeadingLink> hypermdiaLinks = new LinkedList<>();
		if (externalLinks.size() > 0) {
			HeadingLink externalMediaLinks = HeadingLink.builder().type(EXTERNAL.getValue()).count(externalLinks.size())
					.build();
			hypermdiaLinks.add(externalMediaLinks);

		}
		if (mediaLinkList.size() > 0) {
			HeadingLink internalMediaLinks = HeadingLink.builder().type(INTERNAL.getValue()).count(mediaLinkList.size())
					.build();
			hypermdiaLinks.add(internalMediaLinks);
		}

		return hypermdiaLinks;
	}

	@Override
	public List<LinkDetails> getLinkDetails(Document document) {
		Elements elements = document.select(LINK_TAG.getValue());
		int perQuery = QUERY_PER_THREAD.getNumericValue();
		int totalSize = elements.size();
		int start = 0;
		int end = totalSize < perQuery ? totalSize : perQuery;
		List<CompletableFuture<List<LinkDetails>>> futures = new ArrayList<>();
		while (start < totalSize) {
			futures.add(getPartialLinkDetails(elements.subList(start, end)));
			start = end;
			end = (end + perQuery) < totalSize ? (end + perQuery) : totalSize;
		}

		CompletableFuture<Void> finalResult = CompletableFuture
				.allOf(futures.toArray(new CompletableFuture[futures.size()]));

		while (!finalResult.isDone()) {
			continue;
		}
		List<LinkDetails> data = new ArrayList<>();
		for (CompletableFuture<List<LinkDetails>> completableFuture : futures) {
			try {
				data.addAll(completableFuture.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public boolean isLoginFormPresent(Document document) {
		Elements elements = document.select(FORM.getValue());
		boolean isLoginForm = false;
		for (Element element : elements) {
			Elements inputBoxElements = element.select(HTML_TEXT_TAG.getValue());
			if (inputBoxElements == null || (inputBoxElements != null && inputBoxElements.size() > 1)) {
				continue;
			}
			Elements passwordBoxElements = element.select(HTML_PASSWORD_TAG.getValue());
			if (passwordBoxElements == null || (passwordBoxElements != null && passwordBoxElements.size() > 1)) {
				continue;
			}
			Elements sumbmitButtonElements = element.select(HTML_SUBMIT_TAG.getValue());
			if (sumbmitButtonElements == null) {
				continue;
			}
			isLoginForm = true;
		}
		return isLoginForm;
	}

	private HeadingLink getHeadingLink(Document document, String tag) {
		Elements elements = document.select(tag);
		return elements.size() > 0 ? HeadingLink.builder().type(tag).count(elements.size()).build() : null;
	}

	private CompletableFuture<List<LinkDetails>> getPartialLinkDetails(List<Element> links) {
		return CompletableFuture.<List<LinkDetails>> supplyAsync(() -> {
			List<LinkDetails> linkDetails = new ArrayList<>();
			for (Element element : links) {
				String url = element.absUrl(HREF.getValue());
				String msg = null;
				boolean isLinkReachable = false;
				try {
					isLinkReachable = isLinkReachable(url);
				} catch (IOException e) {
					msg = e.getMessage();
				}
				linkDetails.add(LinkDetails.builder().isLinkReachable(isLinkReachable).linkUrl(url).error(msg).build());
			}
			return linkDetails;
		});

	}

	private boolean isLinkReachable(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		if (document != null) {
			return true;
		}
		return false;
	}

}
