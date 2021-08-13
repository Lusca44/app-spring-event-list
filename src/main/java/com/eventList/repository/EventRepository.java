package com.eventList.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventList.models.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
