package com.eventList.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventList.models.Event;
import com.eventList.models.Guest;
import com.eventList.repository.EventRepository;
import com.eventList.repository.GuestRepository;

@Controller
public class EventController {

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private GuestRepository GuestRepo;

	@RequestMapping(value = "/registerEvent", method = RequestMethod.GET)
	public String form() {
		return "evento/formEvent";
	}

	@RequestMapping(value = "/registerEvent", method = RequestMethod.POST)
	public String form(@Valid Event event, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message", "Check the fieds!");
			return "redirect:/registerEvent"; 
		}
		eventRepo.save(event);
		return "redirect:/events";
	}

	@RequestMapping(value = "/events")
	public ModelAndView listEvents() {
		ModelAndView modelAndView = new ModelAndView("index");
		Iterable<Event> events = eventRepo.findAll();
		return modelAndView.addObject("events", events);
	}
	
	@RequestMapping("/delete")
	public String deleteEvent(Long id) {
		
		eventRepo.deleteById(id);
		
		return "redirect:/events";
	}
	
	// =============================  EVENT DETAILS  ======================================================
	//                                ↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ModelAndView eventDetails(@PathVariable("id") Long id) {
		Optional<Event> event = eventRepo.findById(id);
		ModelAndView modelAndView = new ModelAndView("evento/eventDetails");
		modelAndView.addObject("Event", event.get());
		
		Iterable<Guest> guest = GuestRepo.findByEvent(event.get());
		modelAndView.addObject("Guests", guest);
		
		return modelAndView;
	}

	@RequestMapping(value="/{id}", method = RequestMethod.POST)
	public String eventDetailsPost(@PathVariable("id") Long id, @Valid Guest guest, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("message", "Check the fieds!");
			return "redirect:/{id}"; 
		}
		
		Optional<Event> event = eventRepo.findById(id);
		guest.setEvent(event.get());
		
		GuestRepo.save(guest);
		
		attributes.addFlashAttribute("message", "Guest successfuly added!");
		
		return "redirect:/{id}";
	}
	
	@RequestMapping("/deleta")
	public String deleteGuest(String rg) {
		
		Guest guest = GuestRepo.findByRg(rg);
		GuestRepo.delete(guest);
		
		Event event = guest.getEvent();
		
		return "redirect:/" + event.getId();
	}
}
