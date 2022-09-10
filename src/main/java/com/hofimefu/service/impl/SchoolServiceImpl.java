package com.hofimefu.service.impl;

import com.hofimefu.domain.School;
import com.hofimefu.repository.SchoolRepository;
import com.hofimefu.service.SchoolService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link School}.
 */
@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);

    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public School save(School school) {
        log.debug("Request to save School : {}", school);
        return schoolRepository.save(school);
    }

    @Override
    public School update(School school) {
        log.debug("Request to save School : {}", school);
        return schoolRepository.save(school);
    }

    @Override
    public Optional<School> partialUpdate(School school) {
        log.debug("Request to partially update School : {}", school);

        return schoolRepository
            .findById(school.getId())
            .map(existingSchool -> {
                if (school.getName() != null) {
                    existingSchool.setName(school.getName());
                }
                if (school.getAdress() != null) {
                    existingSchool.setAdress(school.getAdress());
                }
                if (school.getEmailDomain() != null) {
                    existingSchool.setEmailDomain(school.getEmailDomain());
                }

                return existingSchool;
            })
            .map(schoolRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<School> findAll() {
        log.debug("Request to get all Schools");
        return schoolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<School> findOne(Long id) {
        log.debug("Request to get School : {}", id);
        return schoolRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete School : {}", id);
        schoolRepository.deleteById(id);
    }
}
