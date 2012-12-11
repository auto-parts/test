package sk.igaraz.tecdoc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import sk.igaraz.db.AutoGarageDbUtil;
import sk.igaraz.db.TecdocDb;
import sk.igataz.tecdoc.data.Article;
import sk.igataz.tecdoc.data.OE;

/**
 * Algoritmus sa sklada z 2 faz
 * 1. vytiahne vsetky OE kody a ku nemu prisluchajuce Article NR. Vysledkom je Map<Article,Set<OE>
 * 2. Kazdy set<OE> rozsire o dalie OE ktore sa nachadzaju v Map<Article,Set<OE>
 * @author ekis
 *
 */
public class OECompatibilitySearcherBackup extends TecdocDb {
	private static final String TABLE_OE = "OE";
	private Map<OE, Set<OE>> oeMap = null;
	long nullOeCount = 0; 
	
	private Map<Article, Set<OE>> phase1() throws Exception {
		Map<Article, Set<OE>> map = new HashMap<Article, Set<OE>>(4000000);
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(constructSQL());
		int i = 0;
		long bigSet = 0;
		double totalSetSize = 0;
		while (rs.next()) {
			String articleString = rs.getString(2);
			String oeString = rs.getString(1);
			Article article = new Article(convertToSearchNumber(articleString), articleString);
			OE oe = new OE(convertToSearchNumber(oeString), oeString);
			Set<OE> set = map.get(article);
			if (set == null) {
				set = new HashSet<OE>(5);
				map.put(article, set);
			} else {
				totalSetSize = totalSetSize + set.size();
			}
			set.add(oe);
			
			if (set.size() > bigSet) {
				bigSet = set.size();
			}
			i++;
			if ( i % 5000 == 0) {
				System.out.println(i+" map: "+map.size()+" big set :" +bigSet+" average "+(double)totalSetSize / (double) i);
			}
			
		}
		
		rs.close();
		st.close();
		conn.close();
		System.out.println("Phase 1 has "+map.size()+" rows");
		return map;
		
	}

	private String constructSQL() {
		return "select TOF_ART_LOOKUP.ARL_SEARCH_NUMBER, TOF_ARTICLES.ART_ARTICLE_NR from TOF_ART_LOOKUP join TECDOC.TOF_ARTICLES on TOF_ART_LOOKUP.ARL_ART_ID=TOF_ARTICLES.ART_ID where TOF_ART_LOOKUP.ARL_KIND='3' order by TOF_ARTICLES.ART_ARTICLE_NR";
	}
	
	public void run() throws Exception {
		System.out.println("Phase 1 start: "+new Date());
		Map<Article, Set<OE>> map = phase1();
		System.out.println("Phase 1 end: "+new Date());
		Map<OE, Set<OE>> result = phase2(map);
		System.out.println("Phase 2 end: "+new Date());
		AutoGarageDbUtil db = new AutoGarageDbUtil();
		db.deleteTable(TABLE_OE);
		db.exportOESet(result, TABLE_OE);
		System.out.println("Commit end: "+new Date());
	}
	
	public void run1() throws Exception {
		System.out.println("Phase 1 start: "+new Date());
		Map<Article, Set<OE>> map = phase1();
		System.out.println("Phase 1 end: "+new Date());
		Map<OE, Set<OE>> result = phase3(map);
		System.out.println("Phase 2 end: "+new Date());
		AutoGarageDbUtil db = new AutoGarageDbUtil();
		db.deleteTable(TABLE_OE);
		db.exportOESet(result, TABLE_OE);
		
		System.out.println("Commit end: "+new Date());
	}
	
	public Map<OE, Set<OE>> phase2(Map<Article, Set<OE>> articleMap) {
		oeMap = new HashMap<OE, Set<OE>>();
		for (Set<OE> oeSet : articleMap.values()) {
			for (OE oe : oeSet) {
				Set<OE> oeSetTemp = oeMap.get(oe);
				if (oeSetTemp == null) {
					oeSetTemp = new LinkedHashSet<OE>();
					oeSetTemp.addAll(oeSet);
					oeMap.put(oe, oeSetTemp);
				} else {
					oeSetTemp.addAll(oeSet);
				}
			}
		}
		return oeMap;
	}
	
	public Map<OE, Set<OE>> phase3(Map<Article, Set<OE>> articleMap) {
		oeMap = new HashMap<OE, Set<OE>>();
		for (Set<OE> oeSet : articleMap.values()) {
			Set<OE> component = findComponentPhase3(oeSet);
		}
		return oeMap;
	}

	private Set<OE> findComponentPhase3(Set<OE> oeSet) {
		return null;
	}

	public String convertToSearchNumber(String originNumber) {
		if (originNumber == null) {
			nullOeCount ++;
			System.out.println("nullOeCount : "+nullOeCount);
			return null;
		}
		return  originNumber.replaceAll("[.-]", "").replaceAll(" ", "");
	}
}
