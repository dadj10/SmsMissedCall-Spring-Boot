package com.smsmissedcall.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smsmissedcall.repository.TicketRepository;

@Controller
@RequestMapping(value = "/")
public class TicketController {
	
	@Autowired
	private TicketRepository ticketRepos;

	@GetMapping()
	public String page(Model model, HttpSession session) {
		// je recherche les tickets par ordre decroissant
		model.addAttribute("Tickets", ticketRepos.findAllTicketOrderByDesc());
		return "callmissed/index";
	}

}
