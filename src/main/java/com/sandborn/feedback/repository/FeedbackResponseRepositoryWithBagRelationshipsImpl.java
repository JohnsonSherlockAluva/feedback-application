package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FeedbackResponseRepositoryWithBagRelationshipsImpl implements FeedbackResponseRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FeedbackResponse> fetchBagRelationships(Optional<FeedbackResponse> feedbackResponse) {
        return feedbackResponse.map(this::fetchApplicationUsers);
    }

    @Override
    public Page<FeedbackResponse> fetchBagRelationships(Page<FeedbackResponse> feedbackResponses) {
        return new PageImpl<>(
            fetchBagRelationships(feedbackResponses.getContent()),
            feedbackResponses.getPageable(),
            feedbackResponses.getTotalElements()
        );
    }

    @Override
    public List<FeedbackResponse> fetchBagRelationships(List<FeedbackResponse> feedbackResponses) {
        return Optional.of(feedbackResponses).map(this::fetchApplicationUsers).orElse(Collections.emptyList());
    }

    FeedbackResponse fetchApplicationUsers(FeedbackResponse result) {
        return entityManager
            .createQuery(
                "select feedbackResponse from FeedbackResponse feedbackResponse left join fetch feedbackResponse.applicationUsers where feedbackResponse is :feedbackResponse",
                FeedbackResponse.class
            )
            .setParameter("feedbackResponse", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FeedbackResponse> fetchApplicationUsers(List<FeedbackResponse> feedbackResponses) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, feedbackResponses.size()).forEach(index -> order.put(feedbackResponses.get(index).getId(), index));
        List<FeedbackResponse> result = entityManager
            .createQuery(
                "select distinct feedbackResponse from FeedbackResponse feedbackResponse left join fetch feedbackResponse.applicationUsers where feedbackResponse in :feedbackResponses",
                FeedbackResponse.class
            )
            .setParameter("feedbackResponses", feedbackResponses)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
