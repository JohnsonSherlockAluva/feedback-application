package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeedbackResponse entity.
 *
 * When extending this class, extend FeedbackResponseRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FeedbackResponseRepository extends FeedbackResponseRepositoryWithBagRelationships, JpaRepository<FeedbackResponse, Long> {
    default Optional<FeedbackResponse> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<FeedbackResponse> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<FeedbackResponse> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
