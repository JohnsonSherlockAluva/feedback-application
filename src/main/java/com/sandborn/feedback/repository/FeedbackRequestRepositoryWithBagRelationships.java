package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FeedbackRequestRepositoryWithBagRelationships {
    Optional<FeedbackRequest> fetchBagRelationships(Optional<FeedbackRequest> feedbackRequest);

    List<FeedbackRequest> fetchBagRelationships(List<FeedbackRequest> feedbackRequests);

    Page<FeedbackRequest> fetchBagRelationships(Page<FeedbackRequest> feedbackRequests);
}
