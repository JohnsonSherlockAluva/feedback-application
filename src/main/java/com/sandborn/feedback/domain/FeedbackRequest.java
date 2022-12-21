package com.sandborn.feedback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A FeedbackRequest.
 */
@Entity
@Table(name = "feedback_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "discription")
    private String discription;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToMany
    @JoinTable(
        name = "rel_feedback_request__feedback_about_users",
        joinColumns = @JoinColumn(name = "feedback_request_id"),
        inverseJoinColumns = @JoinColumn(name = "feedback_about_users_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "internalUser", "designations", "feedbackRequest1s", "groups", "feedbackRequests", "feedbackResponses" },
        allowSetters = true
    )
    private Set<ApplicationUser> feedbackAboutUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_feedback_request__feedback_about",
        joinColumns = @JoinColumn(name = "feedback_request_id"),
        inverseJoinColumns = @JoinColumn(name = "feedback_about_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "feedbackRequests" }, allowSetters = true)
    private Set<FeedbackAbout> feedbackAbouts = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_feedback_request__feedback_response",
        joinColumns = @JoinColumn(name = "feedback_request_id"),
        inverseJoinColumns = @JoinColumn(name = "feedback_response_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUsers", "feedbackRequests" }, allowSetters = true)
    private Set<FeedbackResponse> feedbackResponses = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_feedback_request__feedback_to_users",
        joinColumns = @JoinColumn(name = "feedback_request_id"),
        inverseJoinColumns = @JoinColumn(name = "feedback_to_users_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "internalUser", "designations", "feedbackRequest1s", "groups", "feedbackRequests", "feedbackResponses" },
        allowSetters = true
    )
    private Set<ApplicationUser> feedbackToUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_feedback_request__groups",
        joinColumns = @JoinColumn(name = "feedback_request_id"),
        inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUsers", "feedbackRequests" }, allowSetters = true)
    private Set<Groups> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public FeedbackRequest subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDiscription() {
        return this.discription;
    }

    public FeedbackRequest discription(String discription) {
        this.setDiscription(discription);
        return this;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public FeedbackRequest picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public FeedbackRequest pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public FeedbackRequest status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public FeedbackRequest startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public FeedbackRequest endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public FeedbackRequest createdBy(Long createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Set<ApplicationUser> getFeedbackAboutUsers() {
        return this.feedbackAboutUsers;
    }

    public void setFeedbackAboutUsers(Set<ApplicationUser> applicationUsers) {
        this.feedbackAboutUsers = applicationUsers;
    }

    public FeedbackRequest feedbackAboutUsers(Set<ApplicationUser> applicationUsers) {
        this.setFeedbackAboutUsers(applicationUsers);
        return this;
    }

    public FeedbackRequest addFeedbackAboutUsers(ApplicationUser applicationUser) {
        this.feedbackAboutUsers.add(applicationUser);
        applicationUser.getFeedbackRequest1s().add(this);
        return this;
    }

    public FeedbackRequest removeFeedbackAboutUsers(ApplicationUser applicationUser) {
        this.feedbackAboutUsers.remove(applicationUser);
        applicationUser.getFeedbackRequest1s().remove(this);
        return this;
    }

    public Set<FeedbackAbout> getFeedbackAbouts() {
        return this.feedbackAbouts;
    }

    public void setFeedbackAbouts(Set<FeedbackAbout> feedbackAbouts) {
        this.feedbackAbouts = feedbackAbouts;
    }

    public FeedbackRequest feedbackAbouts(Set<FeedbackAbout> feedbackAbouts) {
        this.setFeedbackAbouts(feedbackAbouts);
        return this;
    }

    public FeedbackRequest addFeedbackAbout(FeedbackAbout feedbackAbout) {
        this.feedbackAbouts.add(feedbackAbout);
        feedbackAbout.getFeedbackRequests().add(this);
        return this;
    }

    public FeedbackRequest removeFeedbackAbout(FeedbackAbout feedbackAbout) {
        this.feedbackAbouts.remove(feedbackAbout);
        feedbackAbout.getFeedbackRequests().remove(this);
        return this;
    }

    public Set<FeedbackResponse> getFeedbackResponses() {
        return this.feedbackResponses;
    }

    public void setFeedbackResponses(Set<FeedbackResponse> feedbackResponses) {
        this.feedbackResponses = feedbackResponses;
    }

    public FeedbackRequest feedbackResponses(Set<FeedbackResponse> feedbackResponses) {
        this.setFeedbackResponses(feedbackResponses);
        return this;
    }

    public FeedbackRequest addFeedbackResponse(FeedbackResponse feedbackResponse) {
        this.feedbackResponses.add(feedbackResponse);
        feedbackResponse.getFeedbackRequests().add(this);
        return this;
    }

    public FeedbackRequest removeFeedbackResponse(FeedbackResponse feedbackResponse) {
        this.feedbackResponses.remove(feedbackResponse);
        feedbackResponse.getFeedbackRequests().remove(this);
        return this;
    }

    public Set<ApplicationUser> getFeedbackToUsers() {
        return this.feedbackToUsers;
    }

    public void setFeedbackToUsers(Set<ApplicationUser> applicationUsers) {
        this.feedbackToUsers = applicationUsers;
    }

    public FeedbackRequest feedbackToUsers(Set<ApplicationUser> applicationUsers) {
        this.setFeedbackToUsers(applicationUsers);
        return this;
    }

    public FeedbackRequest addFeedbackToUsers(ApplicationUser applicationUser) {
        this.feedbackToUsers.add(applicationUser);
        applicationUser.getFeedbackRequests().add(this);
        return this;
    }

    public FeedbackRequest removeFeedbackToUsers(ApplicationUser applicationUser) {
        this.feedbackToUsers.remove(applicationUser);
        applicationUser.getFeedbackRequests().remove(this);
        return this;
    }

    public Set<Groups> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    public FeedbackRequest groups(Set<Groups> groups) {
        this.setGroups(groups);
        return this;
    }

    public FeedbackRequest addGroups(Groups groups) {
        this.groups.add(groups);
        groups.getFeedbackRequests().add(this);
        return this;
    }

    public FeedbackRequest removeGroups(Groups groups) {
        this.groups.remove(groups);
        groups.getFeedbackRequests().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackRequest)) {
            return false;
        }
        return id != null && id.equals(((FeedbackRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackRequest{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", discription='" + getDiscription() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
