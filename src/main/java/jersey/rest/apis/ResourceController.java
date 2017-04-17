package jersey.rest.apis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Sandeep L Hegde
 * @author email : sandeephegde1990@gmail.com
 */

@Path("/pulse")
public class ResourceController {
	
	private static final Map<Integer,String> astronautFileDetails = new HashMap<Integer, String>();
	
	private static Map<Integer,String> healthColorMap = new HashMap<Integer, String>();
	
	private static final Map<Integer,String> astronautDetails = new HashMap<Integer, String>();
	
	private static final Map<String,Integer> astronautReverseDetails = new HashMap<String, Integer>();
	
	private static final Map<Integer,String> parameters = new HashMap<Integer, String>();
	
	private static FileLocationHandler fHandler;
	
	private static final Map<String,String> parametersForGraph = new HashMap<String, String>();
	
	private static final Map<String,String> parameterRangeMap = new HashMap<String,String>();
	
	public ResourceController() {
		
		String osType = System.getProperty("os.name");
		System.out.println("Operating system type: "+osType);
		int type = -1;
		if(osType.contains("Mac")) {
			type = 0;
		}else if(osType.contains("Windows")) {
			type = 1;
		}
		fHandler = new FileLocationHandler(type);
		
		healthColorMap.put(0,"green");
		healthColorMap.put(1, "red");
		healthColorMap.put(2, "yellow");
		
		astronautFileDetails.put(1, FileLocationHandler.johnDataFile);
		astronautFileDetails.put(2, FileLocationHandler.lelandDataFile);
		astronautFileDetails.put(3, FileLocationHandler.sunithaDataFile);
		astronautFileDetails.put(4, FileLocationHandler.laDataFile);
		astronautFileDetails.put(5, FileLocationHandler.nicoleDataFile);
		astronautFileDetails.put(6, FileLocationHandler.randyDataFile);
		astronautFileDetails.put(7, FileLocationHandler.charlieDataFile);
		astronautFileDetails.put(8, FileLocationHandler.danielDataFile);
		astronautFileDetails.put(9, FileLocationHandler.kcDataFile);
		astronautFileDetails.put(10, FileLocationHandler.stevenDataFile);
		
		astronautDetails.put(1, "John Herrington");
		astronautDetails.put(2, "Leland Melvin");
		astronautDetails.put(3, "Sunitha Williams");
		astronautDetails.put(4, "La Estela");
		astronautDetails.put(5, "Nicole Stott");
		astronautDetails.put(6, "Randy Bresnik");
		astronautDetails.put(7, "Charlie Camarda");
		astronautDetails.put(8, "Daniel Burbank");
		astronautDetails.put(9, "KC Thomton");
		astronautDetails.put(10, "Steven Nagel");
		
		astronautReverseDetails.put("John Herrington",1);
		astronautReverseDetails.put("Leland Melvin",2);
		astronautReverseDetails.put("Sunitha Williams",3);
		astronautReverseDetails.put("La Estela",4);
		astronautReverseDetails.put("Nicole Stott",5);
		astronautReverseDetails.put("Randy Bresnik",6);
		astronautReverseDetails.put("Charlie Camarda",7);
		astronautReverseDetails.put("Daniel Burbank",8);
		astronautReverseDetails.put("KC Thomton",9);
		astronautReverseDetails.put("Steven Nagel",10);
		
		parameters.put(1, "Heart Rate");
		parameters.put(2, "BP-systolic");
		parameters.put(3, "BP-diastolic");
		parameters.put(4, "Respiration Rate");
		parameters.put(5, "Blood Glucose");
		parameters.put(6, "Activity Count");
		parameters.put(7, "Body Temperature");
		parameters.put(8, "Body Fat Percentage");
		parameters.put(9, "Body Strength");
		parameters.put(10, "Body Oxygen Level");
		
		parameterRangeMap.put("1", "60,120");
		parameterRangeMap.put("2", "100,140");
		parameterRangeMap.put("13", "60,90");
		parameterRangeMap.put("3", "12,18");
		parameterRangeMap.put("4", "79,140");
		parameterRangeMap.put("5", "50,300");
		parameterRangeMap.put("6", "36,38");
		parameterRangeMap.put("7", "20,30");
		parameterRangeMap.put("8", "50,100");
		parameterRangeMap.put("9", "60,100");
		
		parametersForGraph.put("1", "Heart Rate");
		parametersForGraph.put("2", "Bloob Pressure-systolic");
		parametersForGraph.put("13", "Bloob Pressure-diastolic");
		parametersForGraph.put("3", "Respiration Rate");
		parametersForGraph.put("4", "Blood Glucose");
		parametersForGraph.put("5", "Activity Count");
		parametersForGraph.put("6", "Body Temperature");
		parametersForGraph.put("7", "Body Fat Percentage");
		parametersForGraph.put("8", "Body Strength");
		parametersForGraph.put("9", "Body Oxygen Level");
	}

	@POST
	@Path("/historical_data/store")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response storeHistoricalFileData(
			@com.sun.jersey.multipart.FormDataParam("file") InputStream uploadedInputStream) {
		
		try {
			File file = new File(FileLocationHandler.modelFileLocation);
			deleteDir(file); // Invoke recursive method
			file.mkdir();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// saving file
		try {
			FileOutputStream out = new FileOutputStream(new File(FileLocationHandler.historicalDataFileTmp));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(FileLocationHandler.historicalDataFileTmp));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();

			BufferedReader br = null;
			BufferedWriter wr = null;
			FileReader fr = null;
			
			int count = 0;
			try {
				fr = new FileReader(FileLocationHandler.historicalDataFileTmp);
				br = new BufferedReader(fr);
				String sCurrentLine;
				br = new BufferedReader(new FileReader(FileLocationHandler.historicalDataFileTmp));
				wr = new BufferedWriter(new FileWriter(FileLocationHandler.historicalFileStorePath));
				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.startsWith("0") || sCurrentLine.startsWith("1") || sCurrentLine.startsWith("2")) {
						 if(count != 0)
							 wr.newLine();
						 wr.write(sCurrentLine);
						 count++;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
						wr.close();
					}				
					if (fr != null)
						fr.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			Process p = Runtime.getRuntime().exec("cmd /c start "+ FileLocationHandler.bashLocation1);

			final BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = is.readLine()) != null) {
				System.out.println("Input stream contents starts");
				System.out.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		String output = "File successfully uploaded to : " + FileLocationHandler.historicalFileStorePath;
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/health-monitor/start")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startHealthMonitoring(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		String shouldClean = queryParams.getFirst("clean");
		
		if(shouldClean != null && !shouldClean.isEmpty() && shouldClean.equalsIgnoreCase("true")) {
			try {
				PrintWriter writer = new PrintWriter(FileLocationHandler.healthStatusFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.healthStatusFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.healthDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.johnDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.lelandDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.sunithaDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.laDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.nicoleDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.randyDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.charlieDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.danielDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.kcDataFile);
				writer.print("");
				writer.close();
				
				writer = new PrintWriter(FileLocationHandler.stevenDataFile);
				writer.print("");
				writer.close();
			}catch(Exception e) {
				System.out.println("Exception caught: "+e.getMessage());
			}

			System.out.println("Done cleaning all files");
		}
		
		try {
			Runtime.getRuntime().exec("cmd /c start "+ FileLocationHandler.bashLocation2);
			System.out.println("Command execution started");
			/*final BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = is.readLine()) != null) {
				System.out.println("Input stream contents starts");
				System.out.println(line);
			}*/
		}catch(Exception e) {
			System.out.println("Exception caught: "+e.getMessage());
		}

		
		return Response.status(200).build();
	}

	@POST
	@Path("/health-monitor/stop")
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopHealthMonitoring() {
		return Response.status(200).build();
	}

	@GET
	@Path("/astronaut/health-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchHealthStatus(@Context UriInfo ui) throws IOException {

		JSONObject jUberObj = new JSONObject();
		JSONObject jObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		
		File file = new File(FileLocationHandler.healthStatusFile);
		int n_lines = 10;
		int counter = 0;
		ReversedLinesFileReader object = new ReversedLinesFileReader(file);
		String line = object.readLine();
		while (!line.isEmpty() && counter < n_lines) {
			System.out.println(line);
			
			jObj = new JSONObject();
			String healthStatusStr[] = line.split(" "); 
			jObj.put("ID", healthStatusStr[0]);
			jObj.put("AstronautName", healthStatusStr[1]+" "+healthStatusStr[2]);
			jObj.put("time", healthStatusStr[3]);
			jObj.put("healthStatus", healthColorMap.get(Integer.parseInt(healthStatusStr[4])));
			jArr.put(jObj);
			
			line = object.readLine();
			counter++;
		}
		object.close();

		jUberObj.put("health_data", jArr);
		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	@GET
	@Path("/astronaut/individual-health-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchIndividualHealthStatus(@Context UriInfo ui){
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		String astronautName = queryParams.getFirst("astronautName");
		astronautName = astronautName.replace("%20", " ");
		String fileName = astronautFileDetails.get(astronautReverseDetails.get(astronautName));
		JSONObject jUberObj = new JSONObject();	
		JSONObject jObj = new JSONObject();	
		JSONArray jArr = new JSONArray();		
		File file = new File(fileName);	
		int n_lines = 30;	
		int counter = 0;	
		try {	
		ReversedLinesFileReader object = new ReversedLinesFileReader(file);	
		String line = object.readLine();	
		while (!line.isEmpty() && counter < n_lines) {	
		System.out.println(line);		
		jObj = new JSONObject();	
		String healthStatusStr[] = line.split(" "); 	
		int param = Integer.parseInt(healthStatusStr[0]);	
		if(param == 13) {
		counter++;	
		line = object.readLine();
		continue;
		}else {
		jObj.put("healthfactor", parameters.get(param));
		}
		jObj.put("time", healthStatusStr[1]);
		jObj.put("healthvalue", healthStatusStr[2]);
		jObj.put("healthStatus", healthStatusStr[3]);
		jArr.put(jObj);
		line = object.readLine();
		counter++;
		}
		object.close();
		}catch(Exception e) {
		System.out.println("Exception caused");
		}
		jUberObj.put("health_data", jArr);
		return Response.status(200).entity(jUberObj.toString()).build();
	}

	@POST
	@Path("/register")
	public Response registerController(@FormParam("email") String username,@FormParam("signup_password") String password,
	@FormParam("signup_confpassword") String confPasswd) {
	boolean isStored = RestHelper.writeLoginCredential(username, password, confPasswd);
	if(isStored) {
	return Response.status(200).build();
	}else {
	return Response.status(401).build();
	}
	}

	@POST
	@Path("/login")
	public Response loginController(@FormParam("email") String username,
			@FormParam("password") String password) {
		
		Map<String,String> loginMap = RestHelper.getLoginContents();
		
		if(loginMap.containsKey(username)) {
			String passwordFromMap = loginMap.get(username);
			if(password.equals(passwordFromMap)) {
				return Response.status(200).build();
			}else {
				return Response.status(401).build();
			}
		}else {
			return Response.status(401).build();
		}
	}

	@GET
	@Path("/astronaut/data/download")
	@Produces("text/plain")
	public Response getFile(@Context UriInfo ui) {
		
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		String astronautName = queryParams.getFirst("astronautName");
		astronautName = astronautName.replace("%20", " ");
		
		File file = new File(astronautFileDetails.get(astronautReverseDetails.get(astronautName)));

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=\""+astronautName.replace(" ","_")+".txt\"");
		return response.build();

	}
	
	@GET
	@Path("/streaming_data/download")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/pdf"})
	public Response getFile() {
		File file = new File(FileLocationHandler.healthDataFile);

	    Document document = new Document();
	    BufferedReader br = null;
	    
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("Profiles.pdf"));
			document.open();
			
			br = new BufferedReader(new FileReader(file));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				Paragraph paragraph = new Paragraph();
				paragraph.add(sCurrentLine);
				document.add(paragraph);
			}
			
			document.close();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	    File f = new File("Profiles.pdf");
		return Response.ok(f, "application/pdf").build();

	}
	
	@GET
	@Path("/streaming_data/datadownload")
	@Produces("text/plain")
	public Response getDataFile(@Context UriInfo ui) {
		
		 File file = new File(FileLocationHandler.healthDataFile);  
		 ResponseBuilder response = Response.ok((Object) file);  
		 response.header("Content-Disposition","attachment; filename=\"historical_data.txt\"");  
		 return response.build();  
	}

	private void deleteDir(File dir) {
		File[] files = dir.listFiles();

		for (File myFile : files) {
			if (myFile.isDirectory()) {
				deleteDir(myFile);
			}
			myFile.delete();
		}
	}
	
	@GET
	@Path("/parameter-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getParamData(@Context UriInfo ui) throws IOException {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		int param = Integer.parseInt(queryParams.getFirst("parameter"));
		
		JSONObject jUberObj = new JSONObject();
		JSONObject jObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		int n_lines = 10;
		int counter = 0;
		File file = new File(FileLocationHandler.paramFile);
		ReversedLinesFileReader object = new ReversedLinesFileReader(file);
		String line = object.readLine();
		while (!line.isEmpty() && counter < n_lines) {
			System.out.println(line);
			jObj = new JSONObject();
			String healthStatusStr[] = line.split(" ");
			jObj.put("AstronautName", healthStatusStr[0] + " " + healthStatusStr[1]);
			jObj.put("time", healthStatusStr[2]);
			jObj.put("healthvalue", healthStatusStr[param + 2].split(":")[1]);
			jObj.put("healthStatus",
					getParamstatus(healthStatusStr[param + 2].split(":")[0], healthStatusStr[param + 2].split(":")[1]));
			jArr.put(jObj);
			line = object.readLine();
			counter++;
		}
		object.close();
		jUberObj.put("health_data", jArr);
		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	private static boolean getParamstatus(String paramName, String paramValue) {
		String range[] = (parameterRangeMap.get(paramName)).split(",");
		int minRange = Integer.parseInt(range[0]);
		int maxRange = Integer.parseInt(range[1]);
		int actualVal = Integer.parseInt(paramValue);
		if (actualVal >= minRange && actualVal <= maxRange) {
			return true;
		}
		return false;
	}
	
	@GET
	@Path("/alarm-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchAlarmStatus() {
		JSONObject jUberObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		String sCurrentLine = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(FileLocationHandler.alarmFile));
			while ((sCurrentLine = br.readLine()) != null) {

				JSONObject jObj = new JSONObject();

				String temp[] = sCurrentLine.split(":");

				jObj.put(temp[0], temp[1]);

				jArr.put(jObj);

			}

		} catch (Exception e) {

			System.out.println("Exception caused");

		}

		jUberObj.put("alarm_data", jArr);

		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	@GET
	@Path("/alarm-status-chart")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchAlarmStatusChart() {
		JSONObject jUberObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		String sCurrentLine = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(FileLocationHandler.alarmFile));
			while ((sCurrentLine = br.readLine()) != null) {

				JSONObject jObj = new JSONObject();

				String temp[] = sCurrentLine.split(":");

				jObj.put(temp[0], temp[1]);

				jArr.put(jObj);

			}

		} catch (Exception e) {

			System.out.println("Exception caused");

		}

		jUberObj.put("alarm_data", jArr);

		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	@GET
	@Path("/health/astronaut-graph-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchGraphData(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		String astronautName = queryParams.getFirst("astronautName");
		astronautName = astronautName.replace("%20", " ");
		String fileName = astronautFileDetails.get(astronautReverseDetails.get(astronautName));
		JSONObject jUberObj = new JSONObject();
		Map<String, List<String>> parameters = new HashMap<>();
		File file = new File(fileName);
		int n_lines = 300;
		int counter = 0;
		ReversedLinesFileReader object = null;
		try {
			object = new ReversedLinesFileReader(file);
			String line = object.readLine();
			while (line != null && !line.isEmpty() && counter < n_lines) {
				String[] params = line.split(" ");
				List<String> values = null;
				if (parameters.containsKey(parametersForGraph.get(params[0]))) {
					values = parameters.get(parametersForGraph.get(params[0]));
				} else {
					values = new ArrayList<>();
					values.add(parametersForGraph.get(params[0]));
				}
				values.add(params[2]);
				parameters.put(parametersForGraph.get(params[0]), values);
				line = object.readLine();
				counter++;
			}
			object.close();
			Set<String> set = parameters.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				jUberObj.put(key, parameters.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception caused");
		}
		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	@GET
	@Path("/health/astronaut-graph-dashboard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchGraphDataDashboard(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		String astronautName = queryParams.getFirst("astronautName");
		astronautName = astronautName.replace("%20", " ");
		String fileName = astronautFileDetails.get(astronautReverseDetails.get(astronautName));
		JSONObject jUberObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		Map<String, List<String>> parameters = new HashMap<>();
		File file = new File(fileName);
		int n_lines = 300;
		int counter = 0;
		ReversedLinesFileReader object = null;
		try {
			object = new ReversedLinesFileReader(file);
			String line = object.readLine();
			while (line != null && !line.isEmpty() && counter < n_lines) {
				String[] params = line.split(" ");
				List<String> values = null;
				if (parameters.containsKey(parametersForGraph.get(params[0]))) {
					values = parameters.get(parametersForGraph.get(params[0]));
				} else {
					values = new ArrayList<>();
					values.add(parametersForGraph.get(params[0]));
				}
				values.add(params[2]);
				parameters.put(parametersForGraph.get(params[0]), values);
				line = object.readLine();
				counter++;
			}
			object.close();
			Set<String> set = parameters.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				jArr.put(parameters.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception caused");
		}
		jUberObj.put("array", jArr);
		return Response.status(200).entity(jUberObj.toString()).build();
	}
}