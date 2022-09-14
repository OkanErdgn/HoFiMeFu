package com.hofimefu.service.impl;

import com.hofimefu.domain.EvetUser;
import com.hofimefu.repository.EvetUserRepository;
import com.hofimefu.service.EvetUserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EvetUser}.
 */
@Service
@Transactional
public class EvetUserServiceImpl implements EvetUserService {

    private final Logger log = LoggerFactory.getLogger(EvetUserServiceImpl.class);

    private final EvetUserRepository evetUserRepository;

    public EvetUserServiceImpl(EvetUserRepository evetUserRepository) {
        this.evetUserRepository = evetUserRepository;
    }

    @Override
    public EvetUser save(EvetUser evetUser) {
        log.debug("Request to save EvetUser : {}", evetUser);
        return evetUserRepository.save(evetUser);
    }

    @Override
    public EvetUser update(EvetUser evetUser) {
        log.debug("Request to save EvetUser : {}", evetUser);
        return evetUserRepository.save(evetUser);
    }

    @Override
    public Optional<EvetUser> partialUpdate(EvetUser evetUser) {
        log.debug("Request to partially update EvetUser : {}", evetUser);

        return evetUserRepository
            .findById(evetUser.getId())
            .map(existingEvetUser -> {
                return existingEvetUser;
            })
            .map(evetUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvetUser> findAll() {
        log.debug("Request to get all EvetUsers");
        return evetUserRepository.findAll();
    }

    public Page<EvetUser> findAllWithEagerRelationships(Pageable pageable) {
        return evetUserRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EvetUser> findOne(Long id) {
        log.debug("Request to get EvetUser : {}", id);
        return evetUserRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EvetUser : {}", id);
        evetUserRepository.deleteById(id);
    }
}
