package sk.igaraz.autoData;

import java.util.ArrayList;
import java.util.List;

import sk.igaraz.autoData.data.Brand;
import sk.igaraz.common.WebExtractor;


public class BrandExtractor extends WebExtractor{
	
	private static final String A_TAG_TO = "</a>";
	private static final String A_TAG_FROM = "<a class=\"marki_blok\"";
	private static final String DEFAULT_PAGE = "http://www.auto-data.net/bg/?f=brands";


	public List<Brand> extractBrand(String url) throws Exception {
		List<Brand> list = new ArrayList<Brand>();
		String source = getPageSource(url);
		List<String> linkList = extractParagrafFromTo(source,A_TAG_FROM,A_TAG_TO);
		long id = 1l;
		for (String link : linkList) {
			Brand brand = new Brand();
			brand.setId(id++);
			brand.setHrefName(extractHref(link));
			brand.setName(extractName(link));
			list.add(brand);
		}
		return list;
	}
	
	
	private String extractName(String link) {
		int start = link.lastIndexOf(">");
		return link.substring(start+1,link.length());
	}


	public static void main(String args[]) throws Exception {
		BrandExtractor extractor = new BrandExtractor();
		List<Brand> list = extractor.extractBrand(DEFAULT_PAGE);
		for (Brand brand : list) {
			System.out.println(brand.getName());
		}
	
	}
	
}
