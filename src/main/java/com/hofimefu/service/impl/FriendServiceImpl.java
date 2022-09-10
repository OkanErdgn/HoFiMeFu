package com.hofimefu.service.impl;

import com.hofimefu.domain.Friend;
import com.hofimefu.repository.FriendRepository;
import com.hofimefu.service.FriendService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Friend}.
 */
@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    private final Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

    private final FriendRepository friendRepository;

    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public Friend save(Friend friend) {
        log.debug("Request to save Friend : {}", friend);
        return friendRepository.save(friend);
    }

    @Override
    public Friend update(Friend friend) {
        log.debug("Request to save Friend : {}", friend);
        return friendRepository.save(friend);
    }

    @Override
    public Optional<Friend> partialUpdate(Friend friend) {
        log.debug("Request to partially update Friend : {}", friend);

        return friendRepository
            .findById(friend.getId())
            .map(existingFriend -> {
                return existingFriend;
            })
            .map(friendRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Friend> findAll() {
        log.debug("Request to get all Friends");
        return friendRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Friend> findOne(Long id) {
        log.debug("Request to get Friend : {}", id);
        return friendRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Friend : {}", id);
        friendRepository.deleteById(id);
    }
}
