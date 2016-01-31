package jersey.rest.apis;

import java.sql.*;

public class MysqlDatabaseHelper {
	// JDBC driver name and database URL
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/profiles";

	// Database credentials
	final String USER = "root";
	final String PASS = "";

	public Connection getConnection() throws Exception {
		Connection conn = null;

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		return conn;
	}
	
    public Profiles convertToProfile(ResultSet rs)
            throws Exception {
		Profiles profiles = new Profiles();
    	while(rs.next()) {
    		Profile profile = new Profile();
    		profile.setId(rs.getInt(1));
    		profile.setTitle(rs.getString(2));
    		profiles.getProfiles().add(profile);
    	}
    	
    	return profiles;
    }

	public Profiles getDetailsFromTitle() throws Exception {

		Connection conn = getConnection();
		Statement stmt = conn.createStatement();

		String sql = "SELECT * FROM profiles";
		
		ResultSet rs = stmt.executeQuery(sql);

		Profiles profiles = convertToProfile(rs);
		rs.close();
		
		return profiles;
	}
}
