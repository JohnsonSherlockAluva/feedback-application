package com.sandborn.feedback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "emailid")
    private String emailid;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "location")
    private String location;

    @Lob
    @Column(name = "profilepic")
    private byte[] profilepic;

    @Column(name = "profilepic_content_type")
    private String profilepicContentType;

    @Column(name = "status")
    private Boolean status;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @ManyToMany
    @JoinTable(
        name = "rel_application_user__designation",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "designation_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUsers" }, allowSetters = true)
    private Set<Designation> designations = new HashSet<>();

    @ManyToMany(mappedBy = "feedbackAboutUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "feedbackAboutUsers", "feedbackAbouts", "feedbackResponses", "feedbackToUsers", "groups" },
        allowSetters = true
    )
    private Set<FeedbackRequest> feedbackRequest1s = new HashSet<>();

    @ManyToMany(mappedBy = "applicationUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUsers", "feedbackRequests" }, allowSetters = true)
    private Set<Groups> groups = new HashSet<>();

    @ManyToMany(mappedBy = "feedbackToUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "feedbackAboutUsers", "feedbackAbouts", "feedbackResponses", "feedbackToUsers", "groups" },
        allowSetters = true
    )
    private Set<FeedbackRequest> feedbackRequests = new HashSet<>();

    @ManyToMany(mappedBy = "applicationUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicationUsers", "feedbackRequests" }, allowSetters = true)
    private Set<FeedbackResponse> feedbackResponses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public ApplicationUser firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public ApplicationUser lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailid() {
        return this.emailid;
    }

    public ApplicationUser emailid(String emailid) {
        this.setEmailid(emailid);
        return this;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public ApplicationUser phonenumber(String phonenumber) {
        this.setPhonenumber(phonenumber);
        return this;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLocation() {
        return this.location;
    }

    public ApplicationUser location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getProfilepic() {
        return this.profilepic;
    }

    public ApplicationUser profilepic(byte[] profilepic) {
        this.setProfilepic(profilepic);
        return this;
    }

    public void setProfilepic(byte[] profilepic) {
        this.profilepic = profilepic;
    }

    public String getProfilepicContentType() {
        return this.profilepicContentType;
    }

    public ApplicationUser profilepicContentType(String profilepicContentType) {
        this.profilepicContentType = profilepicContentType;
        return this;
    }

    public void setProfilepicContentType(String profilepicContentType) {
        this.profilepicContentType = profilepicContentType;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public ApplicationUser status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<Designation> getDesignations() {
        return this.designations;
    }

    public void setDesignations(Set<Designation> designations) {
        this.designations = designations;
    }

    public ApplicationUser designations(Set<Designation> designations) {
        this.setDesignations(designations);
        return this;
    }

    public ApplicationUser addDesignation(Designation designation) {
        this.designations.add(designation);
        designation.getApplicationUsers().add(this);
        return this;
    }

    public ApplicationUser removeDesignation(Designation designation) {
        this.designations.remove(designation);
        designation.getApplicationUsers().remove(this);
        return this;
    }

    public Set<FeedbackRequest> getFeedbackRequest1s() {
        return this.feedbackRequest1s;
    }

    public void setFeedbackRequest1s(Set<FeedbackRequest> feedbackRequests) {
        if (this.feedbackRequest1s != null) {
            this.feedbackRequest1s.forEach(i -> i.removeFeedbackAboutUsers(this));
        }
        if (feedbackRequests != null) {
            feedbackRequests.forEach(i -> i.addFeedbackAboutUsers(this));
        }
        this.feedbackRequest1s = feedbackRequests;
    }

    public ApplicationUser feedbackRequest1s(Set<FeedbackRequest> feedbackRequests) {
        this.setFeedbackRequest1s(feedbackRequests);
        return this;
    }

    public ApplicationUser addFeedbackRequest1(FeedbackRequest feedbackRequest) {
        this.feedbackRequest1s.add(feedbackRequest);
        feedbackRequest.getFeedbackAboutUsers().add(this);
        return this;
    }

    public ApplicationUser removeFeedbackRequest1(FeedbackRequest feedbackRequest) {
        this.feedbackRequest1s.remove(feedbackRequest);
        feedbackRequest.getFeedbackAboutUsers().remove(this);
        return this;
    }

    public Set<Groups> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Groups> groups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.removeApplicationUser(this));
        }
        if (groups != null) {
            groups.forEach(i -> i.addApplicationUser(this));
        }
        this.groups = groups;
    }

    public ApplicationUser groups(Set<Groups> groups) {
        this.setGroups(groups);
        return this;
    }

    public ApplicationUser addGroups(Groups groups) {
        this.groups.add(groups);
        groups.getApplicationUsers().add(this);
        return this;
    }

    public ApplicationUser removeGroups(Groups groups) {
        this.groups.remove(groups);
        groups.getApplicationUsers().remove(this);
        return this;
    }

    public Set<FeedbackRequest> getFeedbackRequests() {
        return this.feedbackRequests;
    }

    public void setFeedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        if (this.feedbackRequests != null) {
            this.feedbackRequests.forEach(i -> i.removeFeedbackToUsers(this));
        }
        if (feedbackRequests != null) {
            feedbackRequests.forEach(i -> i.addFeedbackToUsers(this));
        }
        this.feedbackRequests = feedbackRequests;
    }

    public ApplicationUser feedbackRequests(Set<FeedbackRequest> feedbackRequests) {
        this.setFeedbackRequests(feedbackRequests);
        return this;
    }

    public ApplicationUser addFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.add(feedbackRequest);
        feedbackRequest.getFeedbackToUsers().add(this);
        return this;
    }

    public ApplicationUser removeFeedbackRequest(FeedbackRequest feedbackRequest) {
        this.feedbackRequests.remove(feedbackRequest);
        feedbackRequest.getFeedbackToUsers().remove(this);
        return this;
    }

    public Set<FeedbackResponse> getFeedbackResponses() {
        return this.feedbackResponses;
    }

    public void setFeedbackResponses(Set<FeedbackResponse> feedbackResponses) {
        if (this.feedbackResponses != null) {
            this.feedbackResponses.forEach(i -> i.removeApplicationUser(this));
        }
        if (feedbackResponses != null) {
            feedbackResponses.forEach(i -> i.addApplicationUser(this));
        }
        this.feedbackResponses = feedbackResponses;
    }

    public ApplicationUser feedbackResponses(Set<FeedbackResponse> feedbackResponses) {
        this.setFeedbackResponses(feedbackResponses);
        return this;
    }

    public ApplicationUser addFeedbackResponse(FeedbackResponse feedbackResponse) {
        this.feedbackResponses.add(feedbackResponse);
        feedbackResponse.getApplicationUsers().add(this);
        return this;
    }

    public ApplicationUser removeFeedbackResponse(FeedbackResponse feedbackResponse) {
        this.feedbackResponses.remove(feedbackResponse);
        feedbackResponse.getApplicationUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", emailid='" + getEmailid() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", location='" + getLocation() + "'" +
            ", profilepic='" + getProfilepic() + "'" +
            ", profilepicContentType='" + getProfilepicContentType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
