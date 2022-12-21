package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FeedbackResponseRepositoryWithBagRelationships {
    Optional<FeedbackResponse> fetchBagRelationships(Optional<FeedbackResponse> feedbackResponse);

    List<FeedbackResponse> fetchBagRelationships(List<FeedbackResponse> feedbackResponses);

    Page<FeedbackResponse> fetchBagRelationships(Page<FeedbackResponse> feedbackResponses);
}
