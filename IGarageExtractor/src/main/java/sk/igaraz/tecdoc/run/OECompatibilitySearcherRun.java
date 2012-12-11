package sk.igaraz.tecdoc.run;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sk.igaraz.tecdoc.OECompatibilityExtractor;
import sk.igataz.tecdoc.data.OE;

/**
 * Najde vsetky kompatibilne suciastky na zaklade OE
 * @author ekis
 *
 */
public class OECompatibilitySearcherRun {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		OECompatibilityExtractor searcher = new OECompatibilityExtractor();
		searcher.run();
//		searcher.deleteTable(OECompatibilitySearcher.TABLE_PHASE_1);
//		Set<OE> set1 = new LinkedHashSet<OE>();
//		set1.add(new OE("1", "1"));
//		set1.add(new OE("2", "2"));
//		set1.add(new OE("3", "3"));
//		
//		Set<OE> set2 = new LinkedHashSet<OE>();
//		set2.add(new OE("4", "4"));
//		set2.add(new OE("5", "5"));
//		set2.add(new OE("6", "6"));
//		List<Set<OE>> list = new ArrayList<Set<OE>>();
//		list.add(set1);
//		list.add(set2);
//		
//		searcher.storePhase1(list);
//		searcher.run();
//		Map<Article, Set<OE>> map  = new HashMap<Article, Set<OE>>();
//		Article art1 = new Article("1", "1");
//		Article art2 = new Article("2", "2");
//		Set<OE> set1 = new LinkedHashSet<OE>();
//		set1.add(new OE("1", "1"));
//		set1.add(new OE("2", "2"));
//		set1.add(new OE("3", "3"));
//		
//		Set<OE> set2 = new LinkedHashSet<OE>();
//		set2.add(new OE("1", "1"));
//		set2.add(new OE("5", "5"));
//		
//		map.put(art1, set1);
//		map.put(art2, set2);
//		
//		Map<OE, Set<OE>> result  = searcher.phase2(map);
//		for (OE oe : result.keySet()) {
//			System.out.println("OE: "+oe+" value: "+result.get(oe));
//		}
//		
//		AutoGarageDbUtil db = new AutoGarageDbUtil();
//		db.deleteTable("OE");
//		db.exportOESet(result, "OE");
//		 
	}

}
