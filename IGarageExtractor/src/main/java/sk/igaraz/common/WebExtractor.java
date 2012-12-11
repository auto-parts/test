package sk.igaraz.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class WebExtractor {
	protected static final String MAIN_PAGE = "http://moto.allegro.pl";

	protected String getPageSource(String URLaddress) throws Exception {
		URL page = new URL(URLaddress);
        URLConnection yc = page.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
        	buffer.append(inputLine).append("\n");
        }
        in.close();
        
        return buffer.toString();
	}
	
	protected byte[] getData(String URLaddress) throws Exception {
		URL page = new URL(URLaddress);
        URLConnection yc = page.openConnection();
        return IOUtils.toByteArray(yc.getInputStream());
	}
	
	//TODO: zmenit
	protected String extractAllegroHref(String text) {
		int start = text.indexOf("a href=\"");
		int end = text.indexOf("\"",start+8);
		return text.substring(start+8,end);
	}
	
	public List<String> extractParagrafFromTo(String page, String from, String to, boolean print) {
		List<String> list = new ArrayList<String>();
		int start = 0;
		int end = 0;
		while (true) {
			start = page.indexOf(from,start);
			end = page.indexOf(to,start+1);
			if (start == -1 || end == -1) {
				break;
			}
			list.add(page.substring(start+from.length(), end));
			if (print) {
				System.out.println(page.substring(start+from.length(), end));
			}
			start = end+to.length();
		}
		return list;
	}
	
	public List<String> extractParagrafFromTo(String page, String from, String to) {
		return extractParagrafFromTo(page, from, to,false);
	}
	
	protected String extractHref(String link) {
		List<String> linkList = extractParagrafFromTo(link,"\"","\"");
		return linkList.get(0).replaceAll("&amp;", "&");
	}
	
	protected String removeBlankCharacters(String modelName) {
		Pattern pattern = Pattern.compile("\\S");
        Matcher matcher = pattern.matcher(modelName);
        matcher.find();
        int start = matcher.start();
        int end = 0;
        while (matcher.find()) {
        	end = matcher.start();
        }
        return modelName.substring(start, end);
	}

}
