package com.hofimefu.service.impl;

import com.hofimefu.domain.FriendStatus;
import com.hofimefu.repository.FriendStatusRepository;
import com.hofimefu.service.FriendStatusService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FriendStatus}.
 */
@Service
@Transactional
public class FriendStatusServiceImpl implements FriendStatusService {

    private final Logger log = LoggerFactory.getLogger(FriendStatusServiceImpl.class);

    private final FriendStatusRepository friendStatusRepository;

    public FriendStatusServiceImpl(FriendStatusRepository friendStatusRepository) {
        this.friendStatusRepository = friendStatusRepository;
    }

    @Override
    public FriendStatus save(FriendStatus friendStatus) {
        log.debug("Request to save FriendStatus : {}", friendStatus);
        return friendStatusRepository.save(friendStatus);
    }

    @Override
    public FriendStatus update(FriendStatus friendStatus) {
        log.debug("Request to save FriendStatus : {}", friendStatus);
        return friendStatusRepository.save(friendStatus);
    }

    @Override
    public Optional<FriendStatus> partialUpdate(FriendStatus friendStatus) {
        log.debug("Request to partially update FriendStatus : {}", friendStatus);

        return friendStatusRepository
            .findById(friendStatus.getId())
            .map(existingFriendStatus -> {
                if (friendStatus.getCreated() != null) {
                    existingFriendStatus.setCreated(friendStatus.getCreated());
                }
                if (friendStatus.getLastChanged() != null) {
                    existingFriendStatus.setLastChanged(friendStatus.getLastChanged());
                }
                if (friendStatus.getStatus() != null) {
                    existingFriendStatus.setStatus(friendStatus.getStatus());
                }

                return existingFriendStatus;
            })
            .map(friendStatusRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendStatus> findAll() {
        log.debug("Request to get all FriendStatuses");
        return friendStatusRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FriendStatus> findOne(Long id) {
        log.debug("Request to get FriendStatus : {}", id);
        return friendStatusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FriendStatus : {}", id);
        friendStatusRepository.deleteById(id);
    }
}
