package jersey.rest.apis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestHelper {
	private static Map<String, String> loginMap = new HashMap<String, String>();

	public static Map<String, String> getLoginContents() {
		String osType = System.getProperty("os.name");
		System.out.println("Operating system type: "+osType);
		int type = -1;
		if(osType.contains("Mac")) {
			type = 0;
		}else if(osType.contains("Windows")) {
			type = 1;
		}
		new FileLocationHandler(type);
		
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FileLocationHandler.loginFileName);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FileLocationHandler.loginFileName));

			while ((sCurrentLine = br.readLine()) != null) {
				String[] loginArgs = sCurrentLine.split(" ");
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

	public static boolean writeLoginCredential(String username, String password, String confirmpassword) {

		String osType = System.getProperty("os.name");
		System.out.println("Operating system type: "+osType);
		int type = -1;
		if(osType.contains("Mac")) {
			type = 0;
		}else if(osType.contains("Windows")) {
			type = 1;
		}
		new FileLocationHandler(type);
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		if (password.equals(confirmpassword)) {
			System.out.println("Passwords match, please login");
		} else {
			System.out.println("Sign up has issues: Password and Confirm Password do not match");
			return false;
		}

		try {
			fw = new FileWriter(FileLocationHandler.loginFileName, true);
			bw = new BufferedWriter(fw);
			bw.write(username + " " + password);
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