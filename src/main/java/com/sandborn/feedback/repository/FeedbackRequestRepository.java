package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeedbackRequest entity.
 *
 * When extending this class, extend FeedbackRequestRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FeedbackRequestRepository extends FeedbackRequestRepositoryWithBagRelationships, JpaRepository<FeedbackRequest, Long> {
    default Optional<FeedbackRequest> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<FeedbackRequest> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<FeedbackRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
