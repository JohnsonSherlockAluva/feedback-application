package com.sandborn.feedback.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sandborn.feedback.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackResponse.class);
        FeedbackResponse feedbackResponse1 = new FeedbackResponse();
        feedbackResponse1.setId(1L);
        FeedbackResponse feedbackResponse2 = new FeedbackResponse();
        feedbackResponse2.setId(feedbackResponse1.getId());
        assertThat(feedbackResponse1).isEqualTo(feedbackResponse2);
        feedbackResponse2.setId(2L);
        assertThat(feedbackResponse1).isNotEqualTo(feedbackResponse2);
        feedbackResponse1.setId(null);
        assertThat(feedbackResponse1).isNotEqualTo(feedbackResponse2);
    }
}
