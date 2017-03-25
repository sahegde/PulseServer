package jersey.rest.apis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestHelper {
	private static String fileName = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST/src/main/resources/loginData.txt";
	private static Map<String,String> loginMap = new HashMap<String, String>();
	
	public static Map<String,String> getLoginContents() {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(fileName));

			while ((sCurrentLine = br.readLine()) != null) {
				String []loginArgs = sCurrentLine.split(" ");
				loginMap.put(loginArgs[0], loginArgs[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return loginMap;
	}
	
	public static boolean writeLoginCredential(String username, String password) {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(fileName,true);
			bw = new BufferedWriter(fw);
			bw.write(username+" "+password);
			bw.newLine();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
}
