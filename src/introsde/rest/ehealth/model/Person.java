package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity // indicates that this class is an entity to persist in DB
@Table(name = "Person") // to whole table must be persisted
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@XmlRootElement
@XmlType(propOrder = {"idPerson", "firstname", "lastname", "birthdate", "measureType" })
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id // defines this attributed as the one that identifies the entity
	@GeneratedValue(generator = "sqlite_person")
	@TableGenerator(name = "sqlite_person", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
	@Column(name = "idPerson")
	private int idPerson;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "firstname")
	private String firstname;

	// @Column(name="username")
	// private String username;

	@Temporal(TemporalType.DATE) // defines the precision of the date attribute
	@Column(name = "birthdate")
	private Date birthdate;
	// @Column(name="email")
	// private String email;

	// mappedBy must be equal to the name of the attribute in
	// HealthMeasureHistory that maps this relation
	// @OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	// private List<HealthMeasureHistory> measureeeee;
	//
	// @XmlElementWrapper(name = "measureHistory")
	// public List<HealthMeasureHistory> getMeasure () {
	// return measureeeee;
	// }

	// mappedBy must be equal to the name of the attribute in LifeStatus that
	// maps this relation
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LifeStatus> lifeStatus;

	@XmlElementWrapper(name = "healthProfile")
	public List<LifeStatus> getMeasureType() {
		return lifeStatus;
	}
	// add below all the getters and setters of all the private attributes

	// getters
	public int getIdPerson() {
		return idPerson;
	}

	public String getLastname() {
		return lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	// public String getUsername(){
	// return username;
	// }
	public Date getBirthdate() {
		return birthdate;
	}
	// public String getEmail(){
	// return email;
	// }

	// setters
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	// public void setUsername(String username){
	// this.username = username;
	// }
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	// public void setEmail(String email){
	// this.email = email;
	// }

	public static Person getPersonById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static List<Person> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	
	public static Person savePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	
	public static Person updatePerson(Person updPerson, Person oldPerson ) {
		
		if (updPerson.firstname == null)
			updPerson.firstname = oldPerson.firstname;
		if (updPerson.lastname == null)
			updPerson.lastname = oldPerson.lastname;
		if (updPerson.birthdate == null)
			updPerson.birthdate = oldPerson.birthdate;			
		
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		updPerson = em.merge(updPerson);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return updPerson;
	}
	
	public static Person addMeasuresAndValues(Person p){
		for (LifeStatus hp : p.lifeStatus) {
			hp.setPerson(p);
		}
		return p;
	}

	public static void removePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}

}