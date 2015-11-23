package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import java.io.IOException;
import java.util.List;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
public class HealthMeasureHistoryResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	int id;
	String measureType;
	int measureId;

	public HealthMeasureHistoryResource(UriInfo uriInfo, Request request, int personId, String measureType,
			int measureId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = personId;
		this.measureType = measureType;
		this.measureId = measureId;
	}

	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	// will work only inside a Java EE application
	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	// @GET
	// @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public List<HealthMeasureHistory> getHealthMeasureHistory2222() {
	// List<HealthMeasureHistory> healthMeasureHistory =
	// HealthMeasureHistory.getAllDeneme(personId, measureType);
	// if (healthMeasureHistory == null)
	// throw new RuntimeException("Get: Person with " + personId + measureType +
	// " not found");
	// return healthMeasureHistory;
	// }

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<HealthMeasureHistory> getHealthMeasureHistory555() {
		List<HealthMeasureHistory> healthMeasureHistory = HealthMeasureHistory.getHealthMeasureHistoryById(measureId);
		if (healthMeasureHistory == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		return healthMeasureHistory;
	}

}