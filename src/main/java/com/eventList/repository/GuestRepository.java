package com.eventList.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventList.models.Event;
import com.eventList.models.Guest;

public interface GuestRepository extends JpaRepository<Guest, String> {

	Iterable<Guest> findByEvent(Event event);
	Guest findByRg (String rg);
}
