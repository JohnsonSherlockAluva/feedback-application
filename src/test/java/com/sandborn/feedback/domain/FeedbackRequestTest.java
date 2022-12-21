package com.sandborn.feedback.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sandborn.feedback.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackRequest.class);
        FeedbackRequest feedbackRequest1 = new FeedbackRequest();
        feedbackRequest1.setId(1L);
        FeedbackRequest feedbackRequest2 = new FeedbackRequest();
        feedbackRequest2.setId(feedbackRequest1.getId());
        assertThat(feedbackRequest1).isEqualTo(feedbackRequest2);
        feedbackRequest2.setId(2L);
        assertThat(feedbackRequest1).isNotEqualTo(feedbackRequest2);
        feedbackRequest1.setId(null);
        assertThat(feedbackRequest1).isNotEqualTo(feedbackRequest2);
    }
}
