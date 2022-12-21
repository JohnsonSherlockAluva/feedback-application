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
 * A Groups.
 */
@Entity
@Table(name = "groups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

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
        name = "rel_groups__application_user",
        joinColumns = @JoinColumn(name = "groups_id"),
        inverseJoinColumns = @JoinColumn(name = "application_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "internalUser", "designations", "feedbackRequest1s", "groups", "feedbackRequests", "feedbackResponses" },
        allowSetters = true
    )
    private Set<ApplicationUser> applicationUsers = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
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

    public Groups id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Groups groupName(String groupName) {
        this.setGroupName(groupName);
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDiscription() {
        return this.discription;
    }

    public Groups discription(String discription) {
        this.setDiscription(discription);
        return this;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Groups picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Groups pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Groups status(Boolean status) {
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

    public Groups applicationUsers(Set<ApplicationUser> applicationUsers) {
        this.setApplicationUsers(applicationUsers);
        return this;
    }

    public Groups addApplicationUser(ApplicationUser applicationUser) {
        this.applicationUsers.add(applicationUser);
        applicationUser.getGroups().add(this);
        return this;
    }

    public Groups removeApplicationUser(ApplicationUser applicationUser) {
        this.applicationUsers.remove(applicationUser);
        applicationUser.getGroups().remove(this);
        return this;
    }

    public Set<FeedbackRequest> getFeedbackRequests() {
        return this.feedbackRequests;
    }

    public void setFeedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        if (this.feedbackRequests != null) {
            this.feedbackRequests.forEach(i -> i.removeGroups(this));
        }
        if (feedbackRequests != null) {
            feedbackRequests.forEach(i -> i.addGroups(this));
        }
        this.feedbackRequests = feedbackRequests;
    }

    public Groups feedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        this.setFeedbackRequests(feedbackRequests);
        return this;
    }

    public Groups addFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.add(feedbackRequest);
        feedbackRequest.getGroups().add(this);
        return this;
    }

    public Groups removeFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.remove(feedbackRequest);
        feedbackRequest.getGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Groups)) {
            return false;
        }
        return id != null && id.equals(((Groups) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Groups{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", discription='" + getDiscription() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
