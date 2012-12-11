package sk.igaraz.tecdoc.run;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sk.igaraz.db.TecdocDb;
/**
 * Vypise vsetky tabulky z tecdocu. A je pripraveny aj vytvorit Inserty do DB
 * @author ekis
 *
 */
public class ExportCSVRun extends TecdocDb {
	private static final String CSV_DELIMETER = ";";
	private static final String EXPORT_DIR = "D:\\Export\\";

	public static void main(String[] args) throws Exception {
		deleteFolder(new File(EXPORT_DIR));
		List<String> tableNames = allTablesNames();
		
		for (String tableName : tableNames) {
			List<String> collumnNames = collumnsTable(tableName);
			if (collumnNames != null && collumnNames.size() > 0 ) {
				//generateCsvFile(tableName, collumnNames);
				
				System.out.println(tableName+"."+collumnNames);
				
			}
		}
		
		

	}

	private static void generateCsvFile(String sFileName, List<String> list)
			throws Exception {
		
		File file = new File(EXPORT_DIR+sFileName+".csv");
		FileWriter writer = new FileWriter(file);
		StringBuffer buffer = new StringBuffer();
		for (String item : list) {
			buffer.append(item).append(CSV_DELIMETER);
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append("\n");
		writer.append(buffer.toString());
		if (list != null && list.size() > 0) {
			writeData( sFileName, list, writer);
		}
		
		
		writer.flush();
		writer.close();
	}

	private static void writeData(String sFileName, List<String> list, FileWriter writer) throws Exception {
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		st = conn.createStatement();
		StringBuffer sql =  new StringBuffer();
		sql.append("SELECT ");
		for (String item : list) {
			sql.append(item).append(",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" FROM ").append(sFileName);
		System.out.println(sql);
		ResultSet rs = st.executeQuery(sql.toString());
		while (rs.next()) {
			StringBuffer output = new StringBuffer();
			for (int i = 1;i<=list.size();i++) {
				if (rs.getObject(i) == null) {
					output.append("null").append(CSV_DELIMETER);
				} else {
					output.append(rs.getObject(i).toString()).append(CSV_DELIMETER);
				}
				
			}
			if (output != null && output.length() > 0) {
				output.deleteCharAt(output.length()-1);
			}
			output.append("\n");
			writer.append(output.toString());
			
		}
		rs.close();
		st.close();
		conn.close();
		
	}

	private static List<String> collumnsTable(String tableName)
			throws Exception, SQLException {
		List<String> list = new ArrayList<String>();
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);

		ResultSetMetaData rsMetaData = rs.getMetaData();
		int rowCount = rsMetaData.getColumnCount();

		for (int i = 0; i < rowCount; i++) {
			int type = rsMetaData.getColumnType(i + 1);
			String typeName = rsMetaData.getColumnTypeName(i + 1);
			if (type == 1 || "blob".equals(typeName) || "clob".equals(typeName)) {
				continue;
			}
			list.add(rsMetaData.getColumnName(i + 1));
			// System.out.print(rsMetaData.getColumnName(i + 1) + "  \t");
			// System.out.print(rsMetaData.getColumnType(i+1) + "  \t");
			// System.out.print(rsMetaData.getColumnDisplaySize(i+1) + "  \t");
			// System.out.println(rsMetaData.getColumnTypeName(i + 1));
		}

		st.close();
		conn.close();
		return list;
	}

	

	public static List<String> allTablesNames() throws Exception {
		Connection conn = getConnection();
		List<String> tables = new ArrayList<String>();
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		boolean nasiel = false;
		while (rs.next()) {
			String tableName = rs.getString(3);
//			if ("TOF_ART_LOOKUP".equals(tableName)) {
//				nasiel = true;
//			}
//			
//			if (nasiel == false) {
//				continue;
//			}
			if (tableName.substring(0, 3).equals("TOF")) {
				tables.add(tableName);
			}

		}
		conn.close();
		return tables;
	}
	
	public static void deleteFolder(File folder) {
		System.out.println("Clear directory");
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	}
}