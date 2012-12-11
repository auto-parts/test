package sk.igaraz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import sk.igataz.tecdoc.data.OE;

public class AutoGarageDbUtil extends AutoGarageDB {
	public void deleteTable(String tableName) throws Exception {
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		String sql = "DELETE FROM \"" + tableName + "\"";
		int delete = st.executeUpdate(sql);
		System.out.println("Delete table "+tableName+" "+delete+" rows");
	}

	public void exportOESet(Map<OE, Set<OE>> map, String tableName) throws Exception {
		Connection conn = getConnection();
		conn.setAutoCommit(false);
		String sql = "INSERT INTO \""+tableName+"\" (\"OE\",\"OEList\") VALUES (?,?)";
		PreparedStatement prest = conn.prepareStatement(sql);
		for (OE oe : map.keySet()) {
			prest.setString(1, oe.getOriginalNumber());
			prest.setString(2, map.get(oe).toString());
			prest.addBatch();
		}
		int count[] = prest.executeBatch();
		conn.commit();
		conn.close();
		System.out.println("Added Successfully "+count.length+" rows");
	}

	public static void main(String[] args) throws Exception {
		AutoGarageDbUtil test = new AutoGarageDbUtil();
		test.deleteTable("OE");
	}
}
