package jersey.rest.apis;

public class FileLocationHandler {

	public static String healthStatusFile;

	public static String healthDataFile;

	public static String johnDataFile;

	public static String lelandDataFile;

	public static String sunithaDataFile;

	public static String laDataFile;

	public static String nicoleDataFile;

	public static String randyDataFile;

	public static String charlieDataFile;

	public static String danielDataFile;

	public static String kcDataFile;

	public static String stevenDataFile;
	
	public static String historicalFileStorePath;
	
	public static String modelFileLocation;
	
	public static String bashLocation1;
	
	public static String bashLocation2;
	
	public static String historicalDataFileTmp;
	
	public static String loginFileName;

	public FileLocationHandler(int type) {
		if (type == 0) {
			healthStatusFile = "/Users/hsandeep/Desktop/gitRepos/"
					+ "astroDataGen/astroDataGen/src/main/resources/healthStatusFile.txt";

			healthDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/healthDataFile.txt";
			
			johnDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/john.txt";
			
			lelandDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/leland.txt";
			
			sunithaDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/sunitha.txt";
			
			laDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/la.txt";
			
			nicoleDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/nicole.txt";
			
			randyDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/randy.txt";
			
			charlieDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/charlie.txt";
			
			danielDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/daniel.txt";
			
			kcDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/kc.txt";
			
			stevenDataFile = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen"
					+ "/src/main/resources/steven.txt";
			
			historicalFileStorePath = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST"
					+ "/src/main/resources/historical_data.txt";
			
			modelFileLocation = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/astroDataGen/"
					+ "src/main/resources/javaLogisticRegressionWithLBFGSModel";
			
			bashLocation1 = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST"
					+ "/src/main/resources/runBashSandeep1.sh";
			
			bashLocation2 = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST"
					+ "/src/main/resources/runBashSandeep2.sh";
			
			historicalDataFileTmp = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/"+
					"Jersey-Jetty-Mysql-REST/src/main/resources/historical_data.tmp";
			
			loginFileName = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST"
					+"/src/main/resources/loginData.txt";

		}else if(type == 1) {
			
		}
	}
}
