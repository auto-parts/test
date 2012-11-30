package sk.ekissko.AllegroExtractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLMatcher {
	public String matchUrl(String text) {
		Pattern p = Pattern.compile("telo*");
		Matcher m = p.matcher(text); // put here the line you want to check
		if(m.find()){
			System.out.println("X");
		    return m.group();
		}
		return null;
	}
}
