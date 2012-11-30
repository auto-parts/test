package sk.ekissko.AllegroExtractor;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		BrowseAllegro browseAllegro = new BrowseAllegro();
//		try {
//			browseAllegro.setUp();
//			browseAllegro.browse();
//			browseAllegro.tearDown();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		URLMatcher urlMather = new URLMatcher();
//		String result = urlMather.matchUrl("<li><h2><a href=\"/czesci-samochodowe-620\">Części samochodowe <span class=\"catCount\" >&nbsp; (7508444)</span></a></h2></li>");
		String result = urlMather.matchUrl("dfsfs telod dsfsd");
		System.out.println(result);
	}

}
