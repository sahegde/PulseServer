package jersey.rest.apis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Sandeep L Hegde
 * @author email : sandeephegde1990@gmail.com
 */

@Path("/pulse")
public class ResourceController {

	@POST
	@Path("/historical_data/store")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response storeHistoricalFileData(@com.sun.jersey.multipart.FormDataParam("file") InputStream uploadedInputStream) {
		String fileLocation = "/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST/src/main/resources/historical_data.txt";
		// saving file
		try {
			FileOutputStream out = new FileOutputStream(new File(fileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(fileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String output = "File successfully uploaded to : " + fileLocation;
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Path("/health-monitor/start")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startHealthMonitoring() {
		return Response.status(200).build();
	}
	
	@POST
	@Path("/health-monitor/stop")
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopHealthMonitoring() {
		return Response.status(200).build();
	}
	
	@GET
	@Path("/health-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchGraphData(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

		int metricTimeFrame = Integer.parseInt(queryParams.getFirst("time_frame"));
		List<String> metricTypes = Arrays.asList(queryParams.getFirst("metric_types").split(","));
		
		List<JSONArray> jMetric = new ArrayList<JSONArray>();
		for (int i = 0; i < metricTypes.size(); i++) {
			jMetric.add(new JSONArray());
		}
		
		int counter = 0;
		JSONObject jUberObj = new JSONObject();
		if(metricTimeFrame == 5) {
			if(metricTypes.contains("temperature")) {
				JSONArray jArr = jMetric.get(counter++);
				jArr.put("35");
				jArr.put("34");
				jArr.put("33");
				jArr.put("33");
				jArr.put("34");
				
				JSONObject jObj = new JSONObject();
				jObj.put("units", "degrees");
				jObj.put("measurements", jArr);
				
				jUberObj.put("temperature", jObj);
			}
			
			if(metricTypes.contains("oxygen_level")) {
				JSONArray jArr = jMetric.get(counter++);
				jArr.put("60");
				jArr.put("61");
				jArr.put("62");
				jArr.put("61");
				jArr.put("60");
				
				JSONObject jObj = new JSONObject();
				jObj.put("units", "mm HG");
				jObj.put("measurements", jArr);

				jUberObj.put("oxygen_level", jObj);
			}
		}
		else if(metricTimeFrame == 10) {
			if(metricTypes.contains("temperature")) {
				JSONArray jArr = jMetric.get(counter++);
				jArr.put("35");
				jArr.put("34");
				jArr.put("33");
				jArr.put("33");
				jArr.put("34");
				jArr.put("35");
				jArr.put("34");
				jArr.put("33");
				jArr.put("33");
				jArr.put("34");
				
				JSONObject jObj = new JSONObject();
				jObj.put("units", "degrees");
				jObj.put("measurements", jArr);

				jUberObj.put("temperature", jObj);
			}
			
			if(metricTypes.contains("oxygen_level")) {
				JSONArray jArr = jMetric.get(counter++);
				jArr.put("60");
				jArr.put("61");
				jArr.put("62");
				jArr.put("61");
				jArr.put("60");
				jArr.put("60");
				jArr.put("61");
				jArr.put("62");
				jArr.put("61");
				jArr.put("60");
				
				JSONObject jObj = new JSONObject();
				jObj.put("units", "mm HG");
				jObj.put("measurements", jArr);

				jUberObj.put("oxygen_level", jObj);
			}
		}
		
		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	@GET
	@Path("/astronaut/health-status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchHealthStatus(@Context UriInfo ui) {
		JSONObject jUberObj = new JSONObject();
		JSONObject jObj = new JSONObject();
		JSONArray jArr = new JSONArray();
		
		jObj.put("AstronautName", "Sandeep");
		jObj.put("time", new Date().getTime());
		jObj.put("healthStatus", "GREEN");
		jArr.put(jObj);
		
		jObj = new JSONObject();
		jObj.put("AstronautName", "Pradeep");
		jObj.put("time", new Date().getTime());
		jObj.put("healthStatus", "RED");
		jArr.put(jObj);
		
		jObj = new JSONObject();
		jObj.put("AstronautName", "Sumana");
		jObj.put("time", new Date().getTime());
		jObj.put("healthStatus", "GREEN");
		jArr.put(jObj);
		
		jUberObj.put("health_data", jArr);
		return Response.status(200).entity(jUberObj.toString()).build();
	}
	
	@POST
	@Path("/register")
	public Response registerController(@FormParam("username") String username,@FormParam("password") String password) {
		boolean isStored = RestHelper.writeLoginCredential(username,  password);
		if(isStored) {
			return Response.status(200).build();
		}else {
			return Response.status(401).build();
		}
	}
	
	@POST
	@Path("/login")
	public Response loginController(@FormParam("username") String username,
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
    @Path("/streaming_data/download")  
    @Produces("text/plain")  
    public Response getFile() {  
        File file = new File("/Users/hsandeep/Desktop/gitRepos/astroDataGen/Jersey-Jetty-Mysql-REST/src/main/resources/streamingData.txt");  
   
        ResponseBuilder response = Response.ok((Object) file);  
        response.header("Content-Disposition","attachment; filename=\"streamingData.txt\"");  
        return response.build();  
   
    }  
	
}