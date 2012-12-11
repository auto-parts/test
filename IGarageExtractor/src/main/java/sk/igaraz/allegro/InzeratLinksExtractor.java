package sk.igaraz.allegro;

import java.util.LinkedHashSet;
import java.util.Set;

import sk.igaraz.common.WebExtractor;

public class InzeratLinksExtractor extends WebExtractor {
	
	public Set<String> getInzeratsList(String web) throws Exception {
		String page = getPageSource(web);
		int start = page.indexOf("<tbody>");
		start = page.indexOf("<tbody>",start+20);
		int end = page.indexOf("</tbody>", start);
		
		return extractHrefFromTbody(page.substring(start, end+8));
	}
	
	private  Set<String> extractHrefFromTbody(String body) {
		Set<String> set = new LinkedHashSet<String>();
		int start = 0;
		while (true) {
			start = body.indexOf("a href=\"",start);
			int end = body.indexOf("\"",start+8);
			if (start == -1) {
				break;
			} else {
				set.add(MAIN_PAGE+body.substring(start+8,end));
				start = start + 10;
			}
		}
		return set;
	}
	
	public static void main(String[] args) throws Exception {
		InzeratLinksExtractor extractor = new InzeratLinksExtractor();
		Set<String> set = extractor.getInzeratsList("http://moto.allegro.pl/bagazniki-boxy-18657");
		for (String link : set) {
			System.out.println(link);
		}
		System.out.println("End");
		
    }
}
