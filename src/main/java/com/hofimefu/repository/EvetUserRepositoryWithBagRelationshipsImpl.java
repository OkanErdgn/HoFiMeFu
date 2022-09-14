package com.hofimefu.repository;

import com.hofimefu.domain.EvetUser;
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
public class EvetUserRepositoryWithBagRelationshipsImpl implements EvetUserRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<EvetUser> fetchBagRelationships(Optional<EvetUser> evetUser) {
        return evetUser.map(this::fetchUsers);
    }

    @Override
    public Page<EvetUser> fetchBagRelationships(Page<EvetUser> evetUsers) {
        return new PageImpl<>(fetchBagRelationships(evetUsers.getContent()), evetUsers.getPageable(), evetUsers.getTotalElements());
    }

    @Override
    public List<EvetUser> fetchBagRelationships(List<EvetUser> evetUsers) {
        return Optional.of(evetUsers).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    EvetUser fetchUsers(EvetUser result) {
        return entityManager
            .createQuery(
                "select evetUser from EvetUser evetUser left join fetch evetUser.users where evetUser is :evetUser",
                EvetUser.class
            )
            .setParameter("evetUser", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<EvetUser> fetchUsers(List<EvetUser> evetUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, evetUsers.size()).forEach(index -> order.put(evetUsers.get(index).getId(), index));
        List<EvetUser> result = entityManager
            .createQuery(
                "select distinct evetUser from EvetUser evetUser left join fetch evetUser.users where evetUser in :evetUsers",
                EvetUser.class
            )
            .setParameter("evetUsers", evetUsers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
