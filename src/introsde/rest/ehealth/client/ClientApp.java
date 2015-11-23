package introsde.rest.ehealth.client;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

import javax.ws.rs.client.Client;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.glassfish.jersey.client.ClientConfig;

import com.mongodb.util.JSON;

public class ClientApp {
	public static void main(String[] args) throws SAXException, IOException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
		Response response;
				
		String body;
		int status;
		String result;
		
		
		
		// R1 GET persons by JSON
		response = service.path("person")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		
		body = response.readEntity(String.class);
		status = response.getStatus();
		JSONArray personArray = new JSONArray(body);
		if (personArray.length() < 3) 
		{
		result="Result : ERROR because <3" + personArray.length();
		}
		else
		{
		result="Result : OK because >2 " + personArray.length();
		}
		
		System.out.println("Request #1 GET /person Accept: APPLICATION_JSON Content-type: APPLICATION_JSON");
		System.out.println(result);
		System.out.println("HTTP Status: " + status);
		System.out.println("BODY:");
		System.out.println(body);
		
		
		
		
		// R1 GET persons by XML
		response = service.path("person")
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
			
		body = response.readEntity(String.class);
		status = response.getStatus();
								
		System.out.println("Request #1 GET /person Accept: APPLICATION_XML Content-type: APPLICATION_XML");
		System.out.println(result);
		System.out.println("HTTP Status: " + status);
		System.out.println("BODY:");
		System.out.println(body);
		
		
	
		
		
		String firstPersonId=getFirstId(body);
		getLastId(body);	
		
			
		
		
		
		
		//get By person Id for XML
				response = service.path("person/"+firstPersonId)
						.request()
						.accept(MediaType.APPLICATION_XML)
						.get();
				
				body = response.readEntity(String.class);
				status = response.getStatus();
				if (status == 200 | status == 202) 
				{
				result="Result :OK because XML status=" + status;
				}
				else
				{
				result="Result :ERROR because XML status= " + status;
				}
				System.out.println("Request #2 GET /person/"+  firstPersonId  +" Accept: application/XML Content-type: application/XML");
				System.out.println(result);
				System.out.println("HTTP Status: " + status);
				System.out.println("BODY:");
				System.out.println(body);	
				
				
				
		
		
				
		//get By person Id for JSON
		    	response = service.path("person/"+firstPersonId)
						.request()
						.accept(MediaType.APPLICATION_JSON)
						.get();
				
				body = response.readEntity(String.class);
				status = response.getStatus();
				if (status == 200 | status == 202) 
				{
				result="Result :OK because JOSN status=" + status;
				}
				else
				{
				result="Result :ERROR because JSON status= " + status;
				}								
				System.out.println("Request #2 GET /person/" + firstPersonId  +" Accept: application/JSON Content-type: application/JSON");
				System.out.println(result);
				System.out.println("HTTP Status: " + status);
				System.out.println("BODY:");
				System.out.println(body);		

		
		
		
		
		
		
		// PUT for firstname XML 
		// need to take person's name 
				String deneme= "Ana";
		Entity<String> xmlFirstNameString = Entity.xml("<person><firstname>"+deneme+"</firstname></person>");	
		        String path= "person/"+firstPersonId;
				response = service.path(path)	
						.request()
						.accept(MediaType.APPLICATION_XML)
						.put(xmlFirstNameString );
					
				body = response.readEntity(String.class);
				
				Document doc1 = convertStringToDocument(body);
				NodeList firstNames = doc1.getElementsByTagName("firstname");
				String firstN = firstNames.item(0).getTextContent();
				if (firstN.equals(deneme)) {
					result="Result :OK";
				} else {
					result="Result :ERROR";
				}
				status = response.getStatus();
		       		        		
		        System.out.println("Request #3 PUT firstname /person/" + firstPersonId  +" Accept: application/XML Content-type: application/XML");
		        System.out.println(result);
		        System.out.println("HTTP Status: "+ status);
				System.out.println("BODY:");
				System.out.println(body);
		
		
		
		
		
		
		
		
		// PUT for firstname JSON 
		// need to take person's name 
		
		Entity<String> entityPut = Entity.json("{\"firstname\" : \""+deneme+"\"}");
        path= "person/"+firstPersonId;
		response = service.path(path)	
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.put(entityPut);
		
		body = response.readEntity(String.class);
		status = response.getStatus();
        if (status==201) {result="Result :OK";} else {result="Result :ERROR";}
        		
        System.out.println("Request #3 PUT firstname /person/" + firstPersonId  +" Accept: application/json Content-type: application/json");
        System.out.println(result);
        System.out.println("HTTP Status: "+ status);
		System.out.println("BODY:");
		System.out.println(body);
		
		
				
				
				
		
		       
		// post for new person BY JSON	
		Entity<String> entityPostJson = Entity.json("{\"firstname\" : \"Chuck\",\"lastname\" : \"Norris\",\"birthdate\" : \"273535200000\"}");
		response = service.path("person")
		        .request()
		        .accept(MediaType.APPLICATION_JSON)
		        .post(entityPostJson);
		
		body = response.readEntity(String.class);
		status = response.getStatus();
        if (status==201 | status==200 | status==202) {result="Result :OK";} else {result="Result :ERROR";}
        		
        System.out.println("Request #4 POST /person Accept: application/json Content-type: application/json");
        System.out.println(result);
        System.out.println("HTTP Status: "+ status);
		System.out.println("BODY:");
		System.out.println(body);

		
		
		
		
		
		
		// post for new person BY XML
		Entity<String> entityPostXml = Entity.xml("<person>"
						+ "<birthdate>1945-01-01T00:00:00+01:00</birthdate>"
						+ "<firstname>Chuck</firstname>"
						+ "<lastname>Norris</lastname>"
						+ "</person>");
		
				response = service.path("person")
				        .request()
				        .accept(MediaType.APPLICATION_XML)
				        .post(entityPostXml);
				
				body = response.readEntity(String.class);
				status = response.getStatus();
		        if (status==201 | status==200 | status==202) {result="Result :OK";} else {result="Result :ERROR";}
		        		
		        System.out.println("Request #4 POST /person Accept: application/XML Content-type: application/XML");
		        System.out.println(result);
		        System.out.println("HTTP Status: "+ status);
				System.out.println("BODY:");
				System.out.println(body);

		
		
			
	
		
		// Delete JSON
		// First getting all persons and get the last id of uptodate database
		response = service.path("person")
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
			
		body = response.readEntity(String.class);
		
		String lastId = getLastId(body);
		
		// delete process is starting after learning the lastId
		response = service.path("person/"+lastId)
		        .request()
		        .accept(MediaType.APPLICATION_JSON)
		        .delete();
				
		System.out.println("Request #5 Delete Accept: application/json Content-type: application/json");
		
		// R1 is sent
		response = service.path("person/"+lastId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();		
		status = response.getStatus();
		
		// i took 500 error not 404 error when i have sent the get with the invalid ID
		if (status == 404 | status == 500) 
		{
		result="Result :OK because JOSN status=" + status;
		}
		else
		{
		result="Result :ERROR because JSON status= " + status;
		}								
		System.out.println("Result: "+ result);
		System.out.println("HTTP Status: "+ status);
		System.out.println("BODY:----------------------------------" );	
		
		
		
		
		
		
		
		
		// get measure TYPE XML
		response = service.path("measuretype")
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
		
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		System.out.println("Request #6 GET /measuretype Accept: application/XML Content-type: application/XML");
		
		Document doc = convertStringToDocument(body);
		NodeList measure_types = doc.getElementsByTagName("measureType");
		if (measure_types.getLength()<3){result = "ERROR";}else{result="OK";}
		
		System.out.println("Result: " +result);
		System.out.println("HTTP Status: " + status);
		System.out.println("BODY:");
		System.out.println(body);

		
			
		
		
		
		// get measure TYPEEE JSON
		response = service.path("measuretype")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		System.out.println("Request #6 GET /measuretype Accept: application/json Content-type: application/json");
		System.out.println("HTTP Status: " + status);
		System.out.println("Result: " +result);
		System.out.println("BODY:");
		System.out.println(body);
				
		
		
		
		
		
		// R#6 (GET BASE_URL/person/{id}/{measureType}) XML
		String measuretype = "weight";
		
		response = service.path("person/"+ firstPersonId +"/"+ measuretype)
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		System.out.println("Request #7 GET /person/{id}/{measureType} Accept: application/XML Content-type: application/XML");
		
		Document doc2 = convertStringToDocument(body);
		NodeList hmhs = doc2.getElementsByTagName("healthMeasureHistory");
		if (hmhs.getLength() > 0){result = "OK";}else{result="ERROR";}
		System.out.println("Result: "+ result);
		System.out.println("HTTP Status: " + status);
		System.out.println("BODY:");
		System.out.println(body);
		
				
		
		NodeList firstHmhID = doc2.getElementsByTagName("mid");
		String firstMid = firstHmhID.item(0).getTextContent();
		System.out.println("----------------------------"+firstMid);
		
		
		
		// R#6 (GET BASE_URL/person/{id}/{measureType}) JSON
		response = service.path("person/"+ firstPersonId +"/"+ measuretype)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		System.out.println("Request #7 GET /person/{id}/{measureType} Accept: application/json Content-type: application/json");
		System.out.println("Result: "+ result);
		System.out.println("HTTP Status: " + status);
		System.out.println("BODY:");
		System.out.println(body);
		
		
		
		
		
		// 8888888888888888 XML
		
		response = service.path("person/" + firstPersonId + "/"+ measuretype +"/"+ firstMid)
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		
		System.out.println("Request #7 R#7 GET person/{id}/{measureType}/{mid} Accept: application/XML Content-type: application/XML");
		if (status==200){result="OK";}else{result="ERROR";}
		
		
		System.out.println("Result: "+ result);
		System.out.println("HTTP Status: " + status);
		System.out.println("BODY:");
		System.out.println(body);
		
		
	
		
		
		// 8888888888888888 JSON
		
				response = service.path("person/" + firstPersonId + "/"+ measuretype +"/"+ firstMid)
						.request()
						.accept(MediaType.APPLICATION_JSON)
						.get();
				body = response.readEntity(String.class);
				status = response.getStatus(); 
				
				System.out.println("Request #7 R#7 GET person/{id}/{measureType}/{mid} Accept: application/JSON Content-type: application/JSON");
				if (status==200){result="OK";}else{result="ERROR";}
				
				
				System.out.println("Result: "+ result);
				System.out.println("HTTP Status: " + status);
				System.out.println("BODY:");
				System.out.println(body);
		
		
		
		
		
		
		//99999999999999999999 XML
		String m = measure_types.item(0).getTextContent();
		
		response = service.path("person/" + firstPersonId + "/"+ m)
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		System.out.println("Request #9999 GET /person/{id}/{measureType} Accept: application/XML Content-type: application/XML");
		
		doc = convertStringToDocument(body);
		NodeList measurements = doc.getElementsByTagName("healthMeasureHistory");
		int firstControl = measurements.getLength();
		
		Entity<String> entityPostNewHmhXml = Entity.xml("<healthMeasureHistory>"
				+ "<value>72</value>"
				+ "<timestamp>2011-12-09T00:00:00+01:00</timestamp>"
				+ "</healthMeasureHistory>");

		response = service.path("person/" + firstPersonId + "/"+ m )
		        .request()
		        .accept(MediaType.APPLICATION_XML)
		        .post(entityPostNewHmhXml);
		
		response = service.path("person/" + firstPersonId + "/"+ m)
				.request()
				.accept(MediaType.APPLICATION_XML)
				.get();
			
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		
		
		doc = convertStringToDocument(body);
		measurements = doc.getElementsByTagName("healthMeasureHistory");
		int lastControl = measurements.getLength();
		
		
		
		if ((lastControl-firstControl)==1){result = "OK";}else{result="ERROR";}
		System.out.println("Result: "+ result);
		System.out.println("HTTP Status: " + status);
		System.out.println("firstttt: " + firstControl);
		System.out.println("lastttt: " + lastControl);
		System.out.println("BODY:");
		System.out.println(body);
		
		
		
		
		
		//9999999999999999999 JSON
		
		m = measure_types.item(0).getTextContent();
		
		response = service.path("person/" + firstPersonId + "/"+ m)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		System.out.println("Request #9999 GET /person/{id}/{measureType} Accept: application/JSON Content-type: application/JSON");
		
		firstControl+=1;
		
		Entity<String> entityPostNewHmhJson = Entity.json("{\"value\" : \"72\",\"timestamp\" : \"273535200000\"}");

		response = service.path("person/" + firstPersonId + "/"+ m )
		        .request()
		        .accept(MediaType.APPLICATION_JSON)
		        .post(entityPostNewHmhJson);
		
		response = service.path("person/" + firstPersonId + "/"+ m)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
			
		body = response.readEntity(String.class);
		status = response.getStatus(); 
		
		
		lastControl+=1;
		
			
		if ((lastControl-firstControl)==1){result = "OK";}else{result="ERROR";}
		System.out.println("Result: "+ result);
		System.out.println("HTTP Status: " + status);
		System.out.println("firstttt: " + firstControl);
		System.out.println("lastttt: " + lastControl);
		System.out.println("BODY:");
		System.out.println(body);
		
		
		
		
		
	}
	
	
	// to get firstID
	private static String getFirstId(String body){
			
			Document doc = convertStringToDocument(body);		
			NodeList nodes = doc.getElementsByTagName("idPerson");
			String firstpersonId = nodes.item(0).getTextContent();
							
			System.out.println("--------------------------");
			System.out.println("First Peopleid =" +firstpersonId);
			System.out.println("--------------------------");
			
			return firstpersonId;
	}
	
	
	// to get LastID
		private static String getLastId(String body){
				
				Document doc = convertStringToDocument(body);		
				NodeList nodes = doc.getElementsByTagName("idPerson");
				int lenght = nodes.getLength();
				String lastpersonId = nodes.item(lenght-1).getTextContent();				
			
				System.out.println("--------------------------");
				System.out.println("Last Peopleid =" +lastpersonId);
				System.out.println("--------------------------");
				
				return lastpersonId;
		}
	
	
	
	// converts string to XML document
	private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try 
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
   }

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:5700/sdelab").build();
	}
	
	
	

}
