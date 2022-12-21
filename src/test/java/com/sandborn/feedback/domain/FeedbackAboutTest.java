package com.sandborn.feedback.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sandborn.feedback.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackAboutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackAbout.class);
        FeedbackAbout feedbackAbout1 = new FeedbackAbout();
        feedbackAbout1.setId(1L);
        FeedbackAbout feedbackAbout2 = new FeedbackAbout();
        feedbackAbout2.setId(feedbackAbout1.getId());
        assertThat(feedbackAbout1).isEqualTo(feedbackAbout2);
        feedbackAbout2.setId(2L);
        assertThat(feedbackAbout1).isNotEqualTo(feedbackAbout2);
        feedbackAbout1.setId(null);
        assertThat(feedbackAbout1).isNotEqualTo(feedbackAbout2);
    }
}
