package com.smsmissedcall.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smsmissedcall.entities.ParametreApi;
import com.smsmissedcall.repository.ParametreApiRepository;

@Controller
@RequestMapping(value = "api")
public class ParametreApiController {

	@Autowired
	private ParametreApiRepository parametreApiRepos;

	@GetMapping()
	public String page(Model model, HttpSession session) {
		ParametreApi api = parametreApiRepos.findOneParametreApi();
		// je recherche les tickets par ordre decroissant
		model.addAttribute("Api", api);
		return "api/index";
	}

	/*
	 * Fonction qui affiche par id
	 */
	public ParametreApi findOne(Long id) {
		ParametreApi api = null;
		try {
			// api = parametreApiRepos.findParamApiById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return api;
	}

	/*
	 * Fonction qui affiche la page de modification
	 */
	@GetMapping(value = "/formupdate")
	public String formupdate(Long id, Model model) {
		try {
			model.addAttribute("API", findOne(id));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "api/update";
	}

	/*
	 * Fonction qui modification
	 */
	@PostMapping(value = "/update")
	public String update(ParametreApi a, BindingResult bindingResult) {
		ParametreApi api = null;
		try {
			if (bindingResult.hasErrors()) {
				return "api/update";
			}
			api = findOne(a.getId());
			if (api != null) {

				a.setDateModification(new Date());
				a = parametreApiRepos.save(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/api";
	}

}
