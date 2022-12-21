package com.sandborn.feedback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A FeedbackResponse.
 */
@Entity
@Table(name = "feedback_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "response")
    private String response;

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

    @ManyToMany
    @JoinTable(
        name = "rel_feedback_response__application_user",
        joinColumns = @JoinColumn(name = "feedback_response_id"),
        inverseJoinColumns = @JoinColumn(name = "application_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "internalUser", "designations", "feedbackRequest1s", "groups", "feedbackRequests", "feedbackResponses" },
        allowSetters = true
    )
    private Set<ApplicationUser> applicationUsers = new HashSet<>();

    @ManyToMany(mappedBy = "feedbackResponses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "feedbackAboutUsers", "feedbackAbouts", "feedbackResponses", "feedbackToUsers", "groups" },
        allowSetters = true
    )
    private Set<FeedbackRequest> feedbackRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackResponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponse() {
        return this.response;
    }

    public FeedbackResponse response(String response) {
        this.setResponse(response);
        return this;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDiscription() {
        return this.discription;
    }

    public FeedbackResponse discription(String discription) {
        this.setDiscription(discription);
        return this;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public FeedbackResponse picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public FeedbackResponse pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public FeedbackResponse status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<ApplicationUser> getApplicationUsers() {
        return this.applicationUsers;
    }

    public void setApplicationUsers(Set<ApplicationUser> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    public FeedbackResponse applicationUsers(Set<ApplicationUser> applicationUsers) {
        this.setApplicationUsers(applicationUsers);
        return this;
    }

    public FeedbackResponse addApplicationUser(ApplicationUser applicationUser) {
        this.applicationUsers.add(applicationUser);
        applicationUser.getFeedbackResponses().add(this);
        return this;
    }

    public FeedbackResponse removeApplicationUser(ApplicationUser applicationUser) {
        this.applicationUsers.remove(applicationUser);
        applicationUser.getFeedbackResponses().remove(this);
        return this;
    }

    public Set<FeedbackRequest> getFeedbackRequests() {
        return this.feedbackRequests;
    }

    public void setFeedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        if (this.feedbackRequests != null) {
            this.feedbackRequests.forEach(i -> i.removeFeedbackResponse(this));
        }
        if (feedbackRequests != null) {
            feedbackRequests.forEach(i -> i.addFeedbackResponse(this));
        }
        this.feedbackRequests = feedbackRequests;
    }

    public FeedbackResponse feedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        this.setFeedbackRequests(feedbackRequests);
        return this;
    }

    public FeedbackResponse addFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.add(feedbackRequest);
        feedbackRequest.getFeedbackResponses().add(this);
        return this;
    }

    public FeedbackResponse removeFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.remove(feedbackRequest);
        feedbackRequest.getFeedbackResponses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackResponse)) {
            return false;
        }
        return id != null && id.equals(((FeedbackResponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackResponse{" +
            "id=" + getId() +
            ", response='" + getResponse() + "'" +
            ", discription='" + getDiscription() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
