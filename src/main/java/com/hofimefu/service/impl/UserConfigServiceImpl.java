package com.hofimefu.service.impl;

import com.hofimefu.domain.UserConfig;
import com.hofimefu.repository.UserConfigRepository;
import com.hofimefu.service.UserConfigService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserConfig}.
 */
@Service
@Transactional
public class UserConfigServiceImpl implements UserConfigService {

    private final Logger log = LoggerFactory.getLogger(UserConfigServiceImpl.class);

    private final UserConfigRepository userConfigRepository;

    public UserConfigServiceImpl(UserConfigRepository userConfigRepository) {
        this.userConfigRepository = userConfigRepository;
    }

    @Override
    public UserConfig save(UserConfig userConfig) {
        log.debug("Request to save UserConfig : {}", userConfig);
        return userConfigRepository.save(userConfig);
    }

    @Override
    public UserConfig update(UserConfig userConfig) {
        log.debug("Request to save UserConfig : {}", userConfig);
        return userConfigRepository.save(userConfig);
    }

    @Override
    public Optional<UserConfig> partialUpdate(UserConfig userConfig) {
        log.debug("Request to partially update UserConfig : {}", userConfig);

        return userConfigRepository
            .findById(userConfig.getId())
            .map(existingUserConfig -> {
                if (userConfig.getShareLocation() != null) {
                    existingUserConfig.setShareLocation(userConfig.getShareLocation());
                }
                if (userConfig.getLanguage() != null) {
                    existingUserConfig.setLanguage(userConfig.getLanguage());
                }

                return existingUserConfig;
            })
            .map(userConfigRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserConfig> findAll() {
        log.debug("Request to get all UserConfigs");
        return userConfigRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserConfig> findOne(Long id) {
        log.debug("Request to get UserConfig : {}", id);
        return userConfigRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserConfig : {}", id);
        userConfigRepository.deleteById(id);
    }
}
