package sk.ekissoft.autoData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageBrowser {
	public String getHTML(String urlToRead) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	            result += "\n"; 
	         }
	         rd.close();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
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
