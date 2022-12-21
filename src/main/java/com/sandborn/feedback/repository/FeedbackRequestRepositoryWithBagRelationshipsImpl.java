package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.FeedbackRequest;
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
public class FeedbackRequestRepositoryWithBagRelationshipsImpl implements FeedbackRequestRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FeedbackRequest> fetchBagRelationships(Optional<FeedbackRequest> feedbackRequest) {
        return feedbackRequest
            .map(this::fetchFeedbackAboutUsers)
            .map(this::fetchFeedbackAbouts)
            .map(this::fetchFeedbackResponses)
            .map(this::fetchFeedbackToUsers)
            .map(this::fetchGroups);
    }

    @Override
    public Page<FeedbackRequest> fetchBagRelationships(Page<FeedbackRequest> feedbackRequests) {
        return new PageImpl<>(
            fetchBagRelationships(feedbackRequests.getContent()),
            feedbackRequests.getPageable(),
            feedbackRequests.getTotalElements()
        );
    }

    @Override
    public List<FeedbackRequest> fetchBagRelationships(List<FeedbackRequest> feedbackRequests) {
        return Optional
            .of(feedbackRequests)
            .map(this::fetchFeedbackAboutUsers)
            .map(this::fetchFeedbackAbouts)
            .map(this::fetchFeedbackResponses)
            .map(this::fetchFeedbackToUsers)
            .map(this::fetchGroups)
            .orElse(Collections.emptyList());
    }

    FeedbackRequest fetchFeedbackAboutUsers(FeedbackRequest result) {
        return entityManager
            .createQuery(
                "select feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackAboutUsers where feedbackRequest is :feedbackRequest",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FeedbackRequest> fetchFeedbackAboutUsers(List<FeedbackRequest> feedbackRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, feedbackRequests.size()).forEach(index -> order.put(feedbackRequests.get(index).getId(), index));
        List<FeedbackRequest> result = entityManager
            .createQuery(
                "select distinct feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackAboutUsers where feedbackRequest in :feedbackRequests",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequests", feedbackRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    FeedbackRequest fetchFeedbackAbouts(FeedbackRequest result) {
        return entityManager
            .createQuery(
                "select feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackAbouts where feedbackRequest is :feedbackRequest",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FeedbackRequest> fetchFeedbackAbouts(List<FeedbackRequest> feedbackRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, feedbackRequests.size()).forEach(index -> order.put(feedbackRequests.get(index).getId(), index));
        List<FeedbackRequest> result = entityManager
            .createQuery(
                "select distinct feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackAbouts where feedbackRequest in :feedbackRequests",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequests", feedbackRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    FeedbackRequest fetchFeedbackResponses(FeedbackRequest result) {
        return entityManager
            .createQuery(
                "select feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackResponses where feedbackRequest is :feedbackRequest",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FeedbackRequest> fetchFeedbackResponses(List<FeedbackRequest> feedbackRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, feedbackRequests.size()).forEach(index -> order.put(feedbackRequests.get(index).getId(), index));
        List<FeedbackRequest> result = entityManager
            .createQuery(
                "select distinct feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackResponses where feedbackRequest in :feedbackRequests",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequests", feedbackRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    FeedbackRequest fetchFeedbackToUsers(FeedbackRequest result) {
        return entityManager
            .createQuery(
                "select feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackToUsers where feedbackRequest is :feedbackRequest",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FeedbackRequest> fetchFeedbackToUsers(List<FeedbackRequest> feedbackRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, feedbackRequests.size()).forEach(index -> order.put(feedbackRequests.get(index).getId(), index));
        List<FeedbackRequest> result = entityManager
            .createQuery(
                "select distinct feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.feedbackToUsers where feedbackRequest in :feedbackRequests",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequests", feedbackRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    FeedbackRequest fetchGroups(FeedbackRequest result) {
        return entityManager
            .createQuery(
                "select feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.groups where feedbackRequest is :feedbackRequest",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<FeedbackRequest> fetchGroups(List<FeedbackRequest> feedbackRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, feedbackRequests.size()).forEach(index -> order.put(feedbackRequests.get(index).getId(), index));
        List<FeedbackRequest> result = entityManager
            .createQuery(
                "select distinct feedbackRequest from FeedbackRequest feedbackRequest left join fetch feedbackRequest.groups where feedbackRequest in :feedbackRequests",
                FeedbackRequest.class
            )
            .setParameter("feedbackRequests", feedbackRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
