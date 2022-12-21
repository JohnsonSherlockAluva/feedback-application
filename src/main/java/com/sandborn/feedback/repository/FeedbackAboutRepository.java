package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackAbout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeedbackAbout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackAboutRepository extends JpaRepository<FeedbackAbout, Long> {}
