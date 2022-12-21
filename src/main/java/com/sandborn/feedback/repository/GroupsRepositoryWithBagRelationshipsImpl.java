package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.Groups;
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
public class GroupsRepositoryWithBagRelationshipsImpl implements GroupsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Groups> fetchBagRelationships(Optional<Groups> groups) {
        return groups.map(this::fetchApplicationUsers);
    }

    @Override
    public Page<Groups> fetchBagRelationships(Page<Groups> groups) {
        return new PageImpl<>(fetchBagRelationships(groups.getContent()), groups.getPageable(), groups.getTotalElements());
    }

    @Override
    public List<Groups> fetchBagRelationships(List<Groups> groups) {
        return Optional.of(groups).map(this::fetchApplicationUsers).orElse(Collections.emptyList());
    }

    Groups fetchApplicationUsers(Groups result) {
        return entityManager
            .createQuery("select groups from Groups groups left join fetch groups.applicationUsers where groups is :groups", Groups.class)
            .setParameter("groups", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Groups> fetchApplicationUsers(List<Groups> groups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, groups.size()).forEach(index -> order.put(groups.get(index).getId(), index));
        List<Groups> result = entityManager
            .createQuery(
                "select distinct groups from Groups groups left join fetch groups.applicationUsers where groups in :groups",
                Groups.class
            )
            .setParameter("groups", groups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
