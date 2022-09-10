package com.hofimefu.service.impl;

import com.hofimefu.domain.GlobalChat;
import com.hofimefu.repository.GlobalChatRepository;
import com.hofimefu.service.GlobalChatService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GlobalChat}.
 */
@Service
@Transactional
public class GlobalChatServiceImpl implements GlobalChatService {

    private final Logger log = LoggerFactory.getLogger(GlobalChatServiceImpl.class);

    private final GlobalChatRepository globalChatRepository;

    public GlobalChatServiceImpl(GlobalChatRepository globalChatRepository) {
        this.globalChatRepository = globalChatRepository;
    }

    @Override
    public GlobalChat save(GlobalChat globalChat) {
        log.debug("Request to save GlobalChat : {}", globalChat);
        return globalChatRepository.save(globalChat);
    }

    @Override
    public GlobalChat update(GlobalChat globalChat) {
        log.debug("Request to save GlobalChat : {}", globalChat);
        return globalChatRepository.save(globalChat);
    }

    @Override
    public Optional<GlobalChat> partialUpdate(GlobalChat globalChat) {
        log.debug("Request to partially update GlobalChat : {}", globalChat);

        return globalChatRepository
            .findById(globalChat.getId())
            .map(existingGlobalChat -> {
                if (globalChat.getMessage() != null) {
                    existingGlobalChat.setMessage(globalChat.getMessage());
                }
                if (globalChat.getCreated() != null) {
                    existingGlobalChat.setCreated(globalChat.getCreated());
                }

                return existingGlobalChat;
            })
            .map(globalChatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalChat> findAll() {
        log.debug("Request to get all GlobalChats");
        return globalChatRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GlobalChat> findOne(Long id) {
        log.debug("Request to get GlobalChat : {}", id);
        return globalChatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GlobalChat : {}", id);
        globalChatRepository.deleteById(id);
    }
}
