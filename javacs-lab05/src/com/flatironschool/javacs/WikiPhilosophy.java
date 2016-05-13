package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = WikiFetcher.getInstance();

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
	 * 2. Ignoring external links, links to the current page, or red links
	 * 3. Stopping when reaching "Philosophy", a page with no links or a page
	 *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// some example code to get you started
		List<String> visited = new ArrayList<String>();

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		visited.add(url);
		String destination = "https://en.wikipedia.org/wiki/Philosophy";
		int parenthesis = 0;
		boolean done = false;

		while (!done) {
			Elements paragraphs = wf.fetchWikipedia(url);

			Element firstPara = paragraphs.get(0);


			Iterable<Node> iter = new WikiNodeIterable(firstPara);
			for (Node node: iter) {
				if (node instanceof TextNode) {
					if(((TextNode) node).text().contains("(")) {
						parenthesis++;
					}
					if(((TextNode) node).text().contains(")")) {
						parenthesis--;
					}
					//System.out.println(node);
				}
				if (node instanceof Element) {
//					System.out.println(node.parent().attr("lang") + " " + node.parent().nodeName());
//					System.out.println(node.attr("font color"));
					String reference = ((Element) node).absUrl("href");
					if(reference != "" && reference != url && node.parent().nodeName() == "p" && parenthesis == 0) {
						if (visited.contains(reference)) {
							done = true;
						}
						visited.add(reference);
						url = reference;
						if(visited.contains(destination)) {
							done = true;
						}
//						System.out.println(node);
						break;
					}		
				}
			}
		}
		
		if (visited.contains(destination)) {
			System.out.println("Philosophy Page Has Been Found!");
		}
		else {
			System.out.println("The program has failed.");
		}

		// the following throws an exception so the test fails
		// until you update the code
		//        String msg = "Complete this lab by adding your code and removing this statement.";
		//        throw new UnsupportedOperationException(msg);

		for(String s: visited) {
			System.out.println(s);
		}
	}
}
