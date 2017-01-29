package com.qumla.domain.question;

import io.katharsis.resource.annotations.JsonApiIncludeByDefault;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qumla.domain.AbstractEntity;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.user.Session;

@JsonApiResource(type="question")
public class Question extends AbstractEntity {
	private static final long serialVersionUID = -7066465202742759098L;

	public transient static final int STATUS_INACTIVE=0; // normally ianctivated by admin
	public transient static final int STATUS_ACTIVE=100;  // normal active
	
	public transient static final int VALIDATED_UNDER=0; // under acceptance
	public transient static final int VALIDATED_VALID=100; // validated
	public transient static final int VALIDATED_INVALID=50; // invalidated
	
	public transient static final int DASHBOARD_NORMAL=0; // default simple dashboard 
	public transient static final int DASHBOARD_ADVANCED=1; // advanced dashboard with location data

	
	private String title;
	private String titleEn;
	private String description;
	private String encodedDescription;
	
	private String url;
	@JsonIgnore
	
	private Session session;
	private boolean owner;
	private LocationData targetLocation;
	private String targetCountry;
	private Boolean mandatory;
	private Integer type;
	private Integer dashboard;
	private String img;
	private Integer status;
	private boolean answered;
	private boolean subscribed;
	@JsonApiIncludeByDefault
	@JsonApiToMany
	private List<Option> options;
	private String color;
	private boolean multiple;
	private String [] tags;
	private Integer validation;
	private Date validTill; 
	private String ticket;
	private String country;
	private String language;
	private Integer answerCount;
	private String [] categories;
	
	public boolean isSerialVersionUID(){
		return true;
	}
	public Question() {
		options=new LinkedList<Option>();
		status=STATUS_ACTIVE;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public LocationData getTargetLocation() {
		return targetLocation;
	}
	public void setTargetLocation(LocationData targetLocation) {
		this.targetLocation = targetLocation;
	}

	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	@Override
	public boolean equals(Object arg0) {
		if(arg0==null) return false;
		if(arg0 instanceof Question){
			return this.getId().equals(((Question) arg0).id);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.getId().toString().length();
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public boolean isOwner() {
		return owner;
	}
	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	public Integer getValidation() {
		return validation;
	}
	public void setValidation(Integer validation) {
		this.validation = validation;
	}
	public Date getValidTill() {
		return validTill;
	}
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTargetCountry() {
		return targetCountry;
	}
	public void setTargetCountry(String targetCountry) {
		this.targetCountry = targetCountry;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getTitleEn() {
		return titleEn;
	}
	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}
	public Integer getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}
	public boolean isClosed(){
		if(status==null) return false;
		return this.status==50;
	}
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	public boolean isSubscribed() {
		return subscribed;
	}
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
	public String getEncodedDescription() {
		return encodedDescription;
	}
	public void setEncodedDescription(String encodedDescription) {
		this.encodedDescription = encodedDescription;
	}
	public Integer getDashboard() {
		return dashboard;
	}
	public void setDashboard(Integer dashboard) {
		this.dashboard = dashboard;
	}
	
}
