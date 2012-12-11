package sk.ekissoft.autoData;

import java.util.ArrayList;
import java.util.List;

import sk.ekissoft.autoData.data.Brand;


public class BrandExtractor extends PageBrowser{
	
	private static final String A_TAG_TO = "</a>";
	private static final String A_TAG_FROM = "<a class=\"marki_blok\"";
	private static final String DEFAULT_PAGE = "http://www.auto-data.net/bg/?f=brands";


	public List<Brand> extractBrand(String url) {
		List<Brand> list = new ArrayList<Brand>();
		String source = getHTML(url);
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


	public static void main(String args[]) {
		BrandExtractor extractor = new BrandExtractor();
		List<Brand> list = extractor.extractBrand(DEFAULT_PAGE);
		for (Brand brand : list) {
			System.out.println(brand.getName());
		}
	
	}
	
}
