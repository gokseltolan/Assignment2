package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.ehealth.model.Person;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name = "HealthMeasureHistory")
// @NamedQuery(name="HealthMeasureHistory.findAll", query="SELECT h FROM
// HealthMeasureHistory h")

@NamedQueries({
	
		@NamedQuery(name = "HealthMeasureHistory.findAll", query = "SELECT h FROM HealthMeasureHistory h"),
		@NamedQuery(name = "HealthMeasureHistory.getHealthMeasureHistoryByIdAndType", query = "SELECT h FROM HealthMeasureHistory h, MeasureDefinition m  WHERE h.person.idPerson = :idP AND h.measureDefinition.idMeasureDef = m.idMeasureDef AND m.measureName = :measureT"),
		@NamedQuery(name = "HealthMeasureHistory.getByMid", query = "SELECT h FROM HealthMeasureHistory h WHERE h.mid = :idM") 
})

@XmlRootElement
@XmlType(propOrder = { "mid", "value", "timestamp" })
public class HealthMeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_mhistory")
	@TableGenerator(name = "sqlite_mhistory", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "HealthMeasureHistory")

	@Column(name = "idMeasureHistory")
	private int mid;

	@Temporal(TemporalType.DATE)
	@Column(name = "timestamp")
	private Date timestamp;

	@Column(name = "value")
	private String value;

	@ManyToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef")
	private MeasureDefinition measureDefinition;

	// notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	public HealthMeasureHistory() {
	}

	public int getMid() {
		return this.mid;
	}

	public void setMid(int idMeasureHistory) {
		this.mid = idMeasureHistory;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlTransient
	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
		this.measureDefinition = param;
	}

	public Person getPerson() {
		return person;
	}

	@XmlTransient
	public void setPerson(Person param) {
		this.person = param;
	}

	// database operations
	public static List<HealthMeasureHistory> getHealthMeasureHistoryById(int idM) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<HealthMeasureHistory> p = em.createNamedQuery("HealthMeasureHistory.getByMid", HealthMeasureHistory.class)
				.setParameter("idM", idM).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static List<HealthMeasureHistory> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<HealthMeasureHistory> list = em
				.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static List<HealthMeasureHistory> getAllDeneme(int id, String measure) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<HealthMeasureHistory> list = em
				.createNamedQuery("HealthMeasureHistory.getHealthMeasureHistoryByIdAndType", HealthMeasureHistory.class)
				.setParameter("idP", id).setParameter("measureT", measure).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static HealthMeasureHistory saveHealthMeasureHistory(int pid, String mType, HealthMeasureHistory hmh) {
		Person newperson = Person.getPersonById(pid);
		hmh.setPerson(newperson);
		MeasureDefinition newMeasureDefinition = MeasureDefinition.getMeasureDefinitionByName(mType);
		hmh.setMeasureDefinition(newMeasureDefinition);
		hmh.setTimestamp(new Date());
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(hmh);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return hmh;
	}


	public static HealthMeasureHistory updateHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static void removeHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}
}
