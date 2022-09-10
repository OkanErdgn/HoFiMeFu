package com.hofimefu.service.impl;

import com.hofimefu.domain.UserSchool;
import com.hofimefu.repository.UserSchoolRepository;
import com.hofimefu.service.UserSchoolService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserSchool}.
 */
@Service
@Transactional
public class UserSchoolServiceImpl implements UserSchoolService {

    private final Logger log = LoggerFactory.getLogger(UserSchoolServiceImpl.class);

    private final UserSchoolRepository userSchoolRepository;

    public UserSchoolServiceImpl(UserSchoolRepository userSchoolRepository) {
        this.userSchoolRepository = userSchoolRepository;
    }

    @Override
    public UserSchool save(UserSchool userSchool) {
        log.debug("Request to save UserSchool : {}", userSchool);
        return userSchoolRepository.save(userSchool);
    }

    @Override
    public UserSchool update(UserSchool userSchool) {
        log.debug("Request to save UserSchool : {}", userSchool);
        return userSchoolRepository.save(userSchool);
    }

    @Override
    public Optional<UserSchool> partialUpdate(UserSchool userSchool) {
        log.debug("Request to partially update UserSchool : {}", userSchool);

        return userSchoolRepository
            .findById(userSchool.getId())
            .map(existingUserSchool -> {
                if (userSchool.getStatus() != null) {
                    existingUserSchool.setStatus(userSchool.getStatus());
                }

                return existingUserSchool;
            })
            .map(userSchoolRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSchool> findAll() {
        log.debug("Request to get all UserSchools");
        return userSchoolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSchool> findOne(Long id) {
        log.debug("Request to get UserSchool : {}", id);
        return userSchoolRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserSchool : {}", id);
        userSchoolRepository.deleteById(id);
    }
}
