package sk.igaraz.tecdoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.igaraz.db.AutoGarageDB;
import sk.igaraz.db.TecdocDb;
import sk.igataz.tecdoc.data.OE;

/**
 * Algoritmus sa sklada z 2 faz 1. vytiahne vsetky OE kody a ku nemu
 * prisluchajuce Article NR. Vysledkom je Map<Article,Set<OE> 2. Kazdy set<OE>
 * rozsire o dalie OE ktore sa nachadzaju v Map<Article,Set<OE>
 * 
 * @author ekis
 * 
 */
public class OECompatibilityExtractor extends TecdocDb {
	public static final String TABLE_PHASE_1 = "OE_PHASE_1";
	public static final String TABLE_OE = "OE";
	long nullOeCount = 0;
	public Long id = 0l;

	private List<Set<OE>> phase1() throws Exception {
		List<Set<OE>> list = new ArrayList<Set<OE>>(1000);  
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		st = conn.createStatement();

		ResultSet rs = st.executeQuery(constructSQL());
		int i = 0;
		long bigSet = 0;
		double totalSetSize = 0;
		String lastArticleString = null;
		Set<OE> set = null;
		while (rs.next()) {
			String articleString = convertToSearchNumber(rs.getString(2));
			String oeString = rs.getString(1);
			// Article article = new
			// Article(convertToSearchNumber(articleString), articleString);
			OE oe = new OE(convertToSearchNumber(oeString), oeString);
			if (articleString == null || !articleString.equals(lastArticleString)) {
				if (set != null && !set.isEmpty()) {
					list.add(set);
					if (list.size() == 1000) {
						storePhase1(list);
						list.clear();
					}
				}
				lastArticleString = articleString;
				set = new HashSet<OE>(5);
			}
			set.add(oe);
			
			
			

			if (set.size() > bigSet) {
				bigSet = set.size();
			}
			i++;
			if (i % 5000 == 0) {
				System.out.println(i  + " big set :" + bigSet + " average " + (double) totalSetSize / (double) i);
				memoryInformation();
			}

		}
		if (list !=null && !list.isEmpty()) {
			storePhase1(list);
		}
		
		rs.close();
		st.close();
		conn.close();
		return null;

	}

	

	private String constructSQL() {
		return "select TOF_ART_LOOKUP.ARL_SEARCH_NUMBER, TOF_ARTICLES.ART_ARTICLE_NR from TOF_ART_LOOKUP join TECDOC.TOF_ARTICLES on TOF_ART_LOOKUP.ARL_ART_ID=TOF_ARTICLES.ART_ID where TOF_ART_LOOKUP.ARL_KIND='3' order by TOF_ARTICLES.ART_ARTICLE_NR";
	}

	public void run() throws Exception {
		deleteTable(TABLE_PHASE_1);
		System.out.println("Phase 1 start: " + new Date());
		List<Set<OE>> list = phase1();
		System.out.println("Phase 1 end: " + new Date());

	}

	public void storePhase1(List<Set<OE>> list) throws Exception {
		Connection conn = AutoGarageDB.getConnection();
		conn.setAutoCommit(false);
		String sql = "INSERT INTO \""+TABLE_PHASE_1+"\" (\"ID\",\"OE_LIST\") VALUES (?,?)";
		PreparedStatement prest = conn.prepareStatement(sql);
		for (Set<OE> set : list) {
			prest.setLong(1, id++);
			prest.setString(2, set.toString());
			prest.addBatch();
		}
		int count[] = prest.executeBatch();
		conn.commit();
		conn.close();
		System.out.println("Added Successfully ");
	}
	
	public void deleteTable(String tableName) throws Exception {
		Connection conn = AutoGarageDB.getConnection();
		Statement st = conn.createStatement();
		String sql = "DELETE FROM \"" + tableName + "\"";
		int delete = st.executeUpdate(sql);
		System.out.println("Delete table "+tableName+" "+delete+" rows");
	}

	

	public String convertToSearchNumber(String originNumber) {
		if (originNumber == null) {
			nullOeCount++;
			System.out.println("nullOeCount : " + nullOeCount);
			return null;
		}
		return originNumber.replaceAll("[.-]", "").replaceAll(" ", "");
	}
	
	public void memoryInformation() {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
		System.out.println(sb.toString());
	}
}
