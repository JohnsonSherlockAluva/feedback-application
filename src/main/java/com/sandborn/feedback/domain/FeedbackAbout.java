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
 * A FeedbackAbout.
 */
@Entity
@Table(name = "feedback_about")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackAbout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "feedbackabout")
    private String feedbackabout;

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

    @ManyToMany(mappedBy = "feedbackAbouts")
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

    public FeedbackAbout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedbackabout() {
        return this.feedbackabout;
    }

    public FeedbackAbout feedbackabout(String feedbackabout) {
        this.setFeedbackabout(feedbackabout);
        return this;
    }

    public void setFeedbackabout(String feedbackabout) {
        this.feedbackabout = feedbackabout;
    }

    public String getDiscription() {
        return this.discription;
    }

    public FeedbackAbout discription(String discription) {
        this.setDiscription(discription);
        return this;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public FeedbackAbout picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public FeedbackAbout pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public FeedbackAbout status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<FeedbackRequest> getFeedbackRequests() {
        return this.feedbackRequests;
    }

    public void setFeedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        if (this.feedbackRequests != null) {
            this.feedbackRequests.forEach(i -> i.removeFeedbackAbout(this));
        }
        if (feedbackRequests != null) {
            feedbackRequests.forEach(i -> i.addFeedbackAbout(this));
        }
        this.feedbackRequests = feedbackRequests;
    }

    public FeedbackAbout feedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        this.setFeedbackRequests(feedbackRequests);
        return this;
    }

    public FeedbackAbout addFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.add(feedbackRequest);
        feedbackRequest.getFeedbackAbouts().add(this);
        return this;
    }

    public FeedbackAbout removeFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.remove(feedbackRequest);
        feedbackRequest.getFeedbackAbouts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackAbout)) {
            return false;
        }
        return id != null && id.equals(((FeedbackAbout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackAbout{" +
            "id=" + getId() +
            ", feedbackabout='" + getFeedbackabout() + "'" +
            ", discription='" + getDiscription() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
