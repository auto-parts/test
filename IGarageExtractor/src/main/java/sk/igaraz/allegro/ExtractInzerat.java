package sk.igaraz.allegro;

import sk.igaraz.allegro.data.Inzerat;
import sk.igaraz.common.WebExtractor;

public class ExtractInzerat extends WebExtractor {
	public Inzerat extractPage(String web) throws Exception {
		Inzerat inzerat = new Inzerat();
		inzerat.setLink(web);
		
		String page = getPageSource(web);
		inzerat.setPrice(extractPrice(page));
		inzerat.setPicture(extractPicture(page));
		inzerat.setUserText(extractUserText(page));
		return inzerat;
	}
	
	private String extractUserText(String page) {
		int start = page.indexOf("<div id=\"userFieldTab\">");
		int end = page.indexOf("/userField -->",start+25);
		if (start == -1 || end == -1) {
			return null;
		}
		return page.substring(start+25,end-10);
	}

	private byte[] extractPicture(String page) throws Exception {
		int start = page.indexOf("og:image\" content=\"");
		int end = page.indexOf("\"",start+20);
		if (start == -1 || end == -1) {
			return null;
		}
		return getData(page.substring(start+19,end));
	}

	protected Double extractPrice(String text) {
		int start = text.indexOf("<strong class=\"price nowrap\">");
		int end = text.indexOf("z",start+30);
		if (start == -1 || end == -1) {
			return null;
		}
		return new Double(text.substring(start+29,end).replaceAll(" ", ""));
	}
	
	public static void main(String[] args) throws Exception {
		ExtractInzerat extractor = new ExtractInzerat();
		extractor.extractPage("http://moto.allegro.pl/farad-n6-480l-boxy-dachowe-bagazniki-warszawa-pack-i2820520506.html");
	}

}
