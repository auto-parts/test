package sk.igaraz.allegro;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.igaraz.common.WebExtractor;

public class MotoAllegroNavigatorExtractor extends WebExtractor {

	private static final String H2 = "h2";
	private static final String START_PAGE = "http://moto.allegro.pl/czesci-samochodowe-620";
	private static final String MAIN_PAGE = "http://moto.allegro.pl";


	public static void main(String[] args) throws Exception {
		MotoAllegroNavigatorExtractor motoAllegroBrowser = new MotoAllegroNavigatorExtractor();
		motoAllegroBrowser.browse(START_PAGE);
		
		System.out.println("End");
		
    }
	
	private void browse(String url) throws Exception {
		String page = getPageSource(url);
		int start = page.indexOf("<ul class=\"category_list_main\">");
		int end = page.indexOf("</ul>", start);
		
		String navigationText = page.substring(start,end);
		if (navigationText.indexOf("<h2 class") > 0) {
			List<String> navigatonList = getNavigationList(navigationText,H2);
			for (String link : navigatonList) {
				System.out.println(link);
			}
			for (String link : navigatonList) {
				browse(MAIN_PAGE+link);
			}
		} 
	}

	private List<String> getNavigationList(String navigationText, String subHeader) {
		Pattern pattern = Pattern.compile("<"+subHeader+" class");
		Matcher matcher = pattern.matcher(navigationText);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			end = navigationText.indexOf(subHeader, end);
			list.add(extractAllegroHref(navigationText.substring(start, end)));
        }
		return list;
	}
	

	
	
}
