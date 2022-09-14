package com.hofimefu.repository;

import com.hofimefu.domain.EvetUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EvetUserRepositoryWithBagRelationships {
    Optional<EvetUser> fetchBagRelationships(Optional<EvetUser> evetUser);

    List<EvetUser> fetchBagRelationships(List<EvetUser> evetUsers);

    Page<EvetUser> fetchBagRelationships(Page<EvetUser> evetUsers);
}
