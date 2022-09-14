package com.hofimefu.service.impl;

import com.hofimefu.domain.Event;
import com.hofimefu.repository.EventRepository;
import com.hofimefu.service.EventService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> partialUpdate(Event event) {
        log.debug("Request to partially update Event : {}", event);

        return eventRepository
            .findById(event.getId())
            .map(existingEvent -> {
                if (event.getLatitude() != null) {
                    existingEvent.setLatitude(event.getLatitude());
                }
                if (event.getLongitude() != null) {
                    existingEvent.setLongitude(event.getLongitude());
                }
                if (event.getHeader() != null) {
                    existingEvent.setHeader(event.getHeader());
                }
                if (event.getDescription() != null) {
                    existingEvent.setDescription(event.getDescription());
                }
                if (event.getCreated() != null) {
                    existingEvent.setCreated(event.getCreated());
                }
                if (event.getPlanned() != null) {
                    existingEvent.setPlanned(event.getPlanned());
                }

                return existingEvent;
            })
            .map(eventRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        log.debug("Request to get all Events");
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }
}
