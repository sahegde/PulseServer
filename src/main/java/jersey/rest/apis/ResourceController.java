package jersey.rest.apis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Sandeep L Hegde
 * @author email : sandeephegde1990@gmail.com
 */

@Path("/webservice")
public class ResourceController {

	@GET
	@Path("/echo/{message}")
	@Produces("text/plain")
	public String showMsg(@Context UriInfo ui) {
		
		//This is just to experiment if the app is working fine or not
	    MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
	    MultivaluedMap<String, String> pathParams = ui.getPathParameters();
	    
	    for (Map.Entry<String, List<String>> e : queryParams.entrySet()) {
	    	System.out.print("Key "+e.getKey());
	        for (String v : e.getValue()) {
	        	System.out.print(v+" ");
	        }
	        System.out.println();
	    }
	    
	    System.out.println("*******");
	    
	    String ret = "";
	   
	    for (Map.Entry<String, List<String>> e : pathParams.entrySet()) {
	    	System.out.print("Key "+e.getKey()+":");
	        for (String v : e.getValue()) {
	        	System.out.print(v+" ");
	        	ret = v;
	        }
	        System.out.println();
	    }
	    
	    return ret;
	}

	@GET
	@Path("/list")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/pdf"})
	public Response getProfile(@QueryParam("type") String type) {
		
		MysqlDatabaseHelper db = new MysqlDatabaseHelper();
		Profiles profiles = new Profiles();
		try {
			profiles = db.getDetailsFromTitle();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Gson jsonConverter = new GsonBuilder().create();
		if(type.equals("json")) {		
			return Response.ok(jsonConverter.toJson(profiles),MediaType.APPLICATION_JSON).build();		
		}else if(type.equals("xml")) {
			try {
				JAXBContext jc = JAXBContext.newInstance(Profiles.class);
	            Marshaller m = jc.createMarshaller();
	            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	            StringWriter sw = new StringWriter();
	            m.marshal( profiles, sw );
	            
	            return Response.ok(sw.toString(),MediaType.APPLICATION_XML).build();            
			}catch(Exception e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}

		}else if(type.equals("pdf")) {
		    Document document = new Document();
		    
			try {
				PdfWriter writer = PdfWriter.getInstance(document,
						new FileOutputStream("Profiles.pdf"));
				document.open();
				document.add(new Paragraph(jsonConverter.toJson(profiles)));
				document.close();
				writer.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		    File f = new File("Profiles.pdf");
		    return Response.ok(f, "application/pdf").build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}
}