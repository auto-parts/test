package sk.igaraz.allegro.run;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import sk.igaraz.allegro.ExtractInzerat;
import sk.igaraz.allegro.InzeratLinksExtractor;
import sk.igaraz.allegro.data.Inzerat;
import sk.igaraz.db.AutoGarageDB;

public class MotoAllegroExtractor extends AutoGarageDB {
	private static final String DB_PK = "db_pk";
	private static final String DB_USER = "db_user";
	private static final String DB_PASSWORD = "db_password";
	private static final String DB_URL = "db_url";
	private static final String END_PAGE = "endPage";
	private static final String START_PAGE = "startPage";
	private static final String URL_LINK = "urlLink";
	private static final String NEXT_PAGE = "?p=";
	private static final String TABLE_ALLEGRO_INZERATS = "ALLEGRO_INZERATS";
	private String urlLink;
	private Integer startPage = 0;
	private Integer endPage = 0 ;
	private String dbUrl;
	private String dbUser;
	private String dbPassword;
	private Long dbPK;
	
	private InzeratLinksExtractor extractor = new InzeratLinksExtractor();
	private ExtractInzerat inzeratExtractor = new ExtractInzerat();
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 1) {
			System.out.println("Program need properties argument");
			System.exit(0);
		}
		MotoAllegroExtractor extractor = new MotoAllegroExtractor();
		extractor.loadConfig(new File(args[0]));
		if (!extractor.controlVariables()) {
			System.exit(0);
		}
		extractor.run();
	}

	private boolean controlVariables() {
		if (getStartPage() > getEndPage()) {
			System.out.println("Start page must be lower than End page");
			return false;
		}
		
		if (StringUtils.isEmpty(getUrlLink()) ||StringUtils.isEmpty(getDbUrl()) || StringUtils.isEmpty(getDbUser())  || StringUtils.isEmpty(getDbPassword()) || getDbPK() == null ) {
			System.out.println("All properties are not set");
			return false;
		}
		return true;
	}

	void loadConfig(File file) throws Exception {
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		setUrlLink(prop.getProperty(URL_LINK));
		setStartPage(Integer.valueOf(prop.getProperty(START_PAGE)));
		setEndPage(Integer.valueOf(prop.getProperty(END_PAGE)));
		setDbPK(Long.valueOf(prop.getProperty(DB_PK)));
		setDbUrl(prop.getProperty(DB_URL));
		setDbUser(prop.getProperty(DB_USER));
		setDbPassword(prop.getProperty(DB_PASSWORD));
	}
	
	void run() throws Exception {
		for (int i=getStartPage();i<=getEndPage();i++) {
			Set<String> inzeratSets = getInzeratLinks(i);
			if (inzeratSets != null && !inzeratSets.isEmpty()) {
				for (String inzeratUrl : inzeratSets) {
					System.out.println("Extract page: "+inzeratUrl);
					Inzerat inzerat = inzeratExtractor.extractPage(inzeratUrl);
					storeInzerat(inzerat);
				}
			}
		}
	}

	private void storeInzerat(Inzerat inzerat) throws Exception {
		Connection conn = AutoGarageDB.getConnection();
		conn.setAutoCommit(false);
		String sql = "INSERT INTO \""+TABLE_ALLEGRO_INZERATS+"\" (\"ID\",\"LINK\") VALUES (?,?)";
		PreparedStatement prest = conn.prepareStatement(sql);
		prest.setLong(1, dbPK++);
		prest.setString(2, inzerat.getLink());
		prest.executeUpdate();
		conn.commit();
		conn.close();
	}

	private Set<String> getInzeratLinks(int i) throws Exception {
		Set<String> inzeratSets = null;
		if (i == 1) {
			inzeratSets = extractor.getInzeratsList(urlLink);
		} else {
			inzeratSets = extractor.getInzeratsList(urlLink+NEXT_PAGE+i);
		}
		return inzeratSets;
	}
	
	public Connection getApplicationConnection() throws Exception {
		Class.forName(DRIVER);
		return DriverManager.getConnection(getDbUrl(), getDbUser(),  getDbPassword());
	}


	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public Long getDbPK() {
		return dbPK;
	}

	public void setDbPK(Long dbPK) {
		this.dbPK = dbPK;
	}
	
}
