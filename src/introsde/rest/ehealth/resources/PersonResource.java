package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE
			// container
@LocalBean // only used if the the application is deployed in a Java EE
			// container
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;

	EntityManager entityManager; // only used if the application is deployed in
									// a Java EE container

	public PersonResource(UriInfo uriInfo, Request request, int id, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
	}

	public PersonResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// Application integration
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person getPerson() {
		Person person = this.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		return person;
	}

	// for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Person getPersonHTML() {
		Person person = this.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		System.out.println("Returning person... " + person.getIdPerson());
		return person;
	}
	
	
	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putPerson(Person person) {
		System.out.println("--> Updating Person... " + this.id);
		System.out.println("--> " + person.toString());
		Response res;
		Person existing = getPersonById(this.id);

		if (existing == null) {
			res = Response.noContent().build();
		} else {
			
			person.setIdPerson(this.id);
			person = Person.updatePerson(person, existing);
			res = Response.created(uriInfo.getAbsolutePath()).entity(person).build();
		}
		return res;
	}

	@DELETE
	public void deletePerson() {
		Person c = getPersonById(id);
		if (c == null)
			throw new RuntimeException("Delete: Person with " + id + " not found");
		Person.removePerson(c);
	}

	
	public Person getPersonById(int personId) {
		System.out.println("Reading person from DB with id: " + personId);

		// this will work within a Java EE container, where not DAO will be
		// needed
		// Person person = entityManager.find(Person.class, personId);

		Person person = Person.getPersonById(personId);
		System.out.println("Person: " + person.toString());
		return person;
	}

	@Path("{measureType}")
	public HealthMeasureHistoryCollectionResource getHealthMeasureHistory1111(
			@PathParam("measureType") String measureType) {
		return new HealthMeasureHistoryCollectionResource(uriInfo, request, this.id, measureType);
	}

}