package sk.igaraz.autoData;

import java.util.ArrayList;
import java.util.List;

import sk.igaraz.autoData.data.Brand;
import sk.igaraz.autoData.data.Model;
import sk.igaraz.common.WebExtractor;

public class ModelExtractor extends WebExtractor{
	private static final String A_TAG_TO = "</a>";
	private static final String A_TAG_FROM = "<a class=\"modeli\"";
	
	private List<Model> extractModel(String url, Brand brand) throws Exception {
		List<Model> list = new ArrayList<Model>();
		String source = getPageSource(url);
		List<String> linkList = extractParagrafFromTo(source,A_TAG_FROM,A_TAG_TO);
		long id = 1l;
		for (String link : linkList) {
			Model model = new Model();
			model.setId(id);
			model.setBrand(brand);
			model.setHrefName(extractHref(link));
			model.setName(removeBlankCharacters(extractName(link)));
			list.add(model);
		}
		return list;
	}
	
	
	private String extractName(String link) {
		List<String> linkList = extractParagrafFromTo(link,">","<br");
		return linkList.get(0);
	}
	
	public static void main(String args[]) throws Exception {
		ModelExtractor extractor = new ModelExtractor();
		List<Model> list = extractor.extractModel("http://www.auto-data.net/bg/?f=showModel&marki_id=72",null);
		for (Model model : list) {
			System.out.println(model.getName());
		}
	
	}
}
