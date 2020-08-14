package com.smsmissedcall.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smsmissedcall.entities.ParamApi;
import com.smsmissedcall.entities.ParamsSqlServer;
import com.smsmissedcall.repository.ParamApiRepository;
import com.smsmissedcall.repository.ParamsSqlServerRepository;

@Controller
@CrossOrigin("*")
@RequestMapping(value = "api")
public class paramsApiController {

	@Autowired
	private ParamApiRepository paramApiRepos;

	@GetMapping()
	public String page(Model model, HttpSession session) {
		// je recherche les tickets par ordre decroissant
		model.addAttribute("Api", paramApiRepos.findOneParamApi());
		return "api/index";
	}

	/*
	 * Fonction qui affiche par id
	 */
	public ParamApi findOne(Long id) {
		ParamApi api = null;
		try {
			api = paramApiRepos.findParamApiById(id);
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
	public String update(ParamApi a, BindingResult bindingResult) {
		ParamApi api = null;
		try {
			if (bindingResult.hasErrors()) {
				return "api/update";
			}

			api = findOne(a.getId());
			if (api != null) {

				a.setMethod_one(api.getMethod_one());
				a.setMethod_full(api.getMethod_full());
				a.setMethod_bulk(api.getMethod_bulk());

				a.setDateModification(new Date());
				a = paramApiRepos.save(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/api";
	}

}
