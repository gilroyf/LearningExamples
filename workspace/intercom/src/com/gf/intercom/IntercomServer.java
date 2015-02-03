package com.gf.intercom;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.json.generators.JSONGenerator;
import com.json.generators.JsonGeneratorFactory;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
//import com.json;//.parser;//.JsonParserFactory

//import com.gfexamples.jeetutorial.Consumes;

@Path("/intercomserver")
public class IntercomServer {
	@Context
    private UriInfo context;
	static private HashMap<String,Map> nameToOfferMap= new HashMap<String, Map>();
	static private HashMap<String,Map> nameToAnswerMap= new HashMap<String, Map>();
	public IntercomServer() { 
//		this.nameToOfferMap = new HashMap<String, String>();
	};
	
	@POST
    @Consumes("text/plain")
	public void getInitiatorOffer(String s) {
		System.out.println("In getInitiatorOffer " + s);
	}
	
	@POST
    @Consumes("application/json")
	public void post(String s) {
		System.out.println("In getInitiatorOfferJSON " + s);
		JsonParserFactory factory=JsonParserFactory.getInstance();
		JSONParser parser=factory.newJsonParser();
		Map jsonData = parser.parseJson(s);
		String type = (String)jsonData.get("type");
		if (type.equals("initiateCall")) {
			String group = (String)jsonData.get("group");
			Map data = (Map)jsonData.get("sdp");
			String sdp = (String)data.get("sdp");
			nameToOfferMap.put(group, data);
			System.out.println("group ="+group);
			System.out.println("sdp = "+sdp);
			System.out.println(" size = "+nameToOfferMap.size());
		} else if (type.equals("answerResponse")) {
			String group = (String)jsonData.get("group");
			Map data = (Map)jsonData.get("sdp");
			String sdp = (String)data.get("sdp");
			nameToAnswerMap.put(group, data);
			System.out.println("group ="+group);
			System.out.println("sdp = "+sdp);
			System.out.println(" size = "+nameToOfferMap.size());
		}
	}
	
	@GET
	@Produces("application/json")
	public String goGetAsPlainText() {
		Map m= nameToOfferMap.get(new String("group1"));
		JsonGeneratorFactory factory=JsonGeneratorFactory.getInstance();
        JSONGenerator generator=factory.newJsonGenerator();
        String s=generator.generateJson(m);
		
		System.out.println("outputting "+s+ " size = "+nameToOfferMap.size());
		if (s != null)
			return s;
		else
			return new String("{error:\"missing value\"}");
	}
	
	@GET
	@Path("{groupName}")
	@Produces("application/json")
	public String getDescForGroupName(@PathParam("groupName") String name) {
		Map m= nameToOfferMap.get(name);
		if (m == null)
			return new String("{\"error\":\"missing value\"}");
		
		JsonGeneratorFactory factory=JsonGeneratorFactory.getInstance();
        JSONGenerator generator=factory.newJsonGenerator();
        String s=generator.generateJson(m);
		
		System.out.println("outputting "+s+ " size = "+nameToOfferMap.size());
		if (s != null)
			return s;
		else
			return new String("{\"error\":\"missing value\"}");
	}
	
	@GET
	@Path("/answer/{groupName}")
	@Produces("application/json")
	public String getAnswerForGroupName(@PathParam("groupName") String name) {
		Map m= nameToAnswerMap.get(name);
		if (m == null)
			return new String("{\"error\":\"missing value\"}");
		JsonGeneratorFactory factory=JsonGeneratorFactory.getInstance();
        JSONGenerator generator=factory.newJsonGenerator();
        String s=generator.generateJson(m);
		
		System.out.println("outputting "+s+ " size = "+nameToOfferMap.size());
		if (s != null)
			return s;
		else
			return new String("{\"error\":\"missing value\"}");
	}
}
