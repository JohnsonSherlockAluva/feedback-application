package com.sandborn.feedback.repository;

import com.sandborn.feedback.domain.Groups;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface GroupsRepositoryWithBagRelationships {
    Optional<Groups> fetchBagRelationships(Optional<Groups> groups);

    List<Groups> fetchBagRelationships(List<Groups> groups);

    Page<Groups> fetchBagRelationships(Page<Groups> groups);
}
