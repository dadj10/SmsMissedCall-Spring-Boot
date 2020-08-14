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

import com.smsmissedcall.entities.ParamsSqlServer;
import com.smsmissedcall.repository.ParamsSqlServerRepository;

@Controller
@CrossOrigin("*")
@RequestMapping(value = "sql-server")
public class paramsSqlServerController {

	@Autowired
	private ParamsSqlServerRepository paramsSqlServerRepos;

	@GetMapping()
	public String page(Model model, HttpSession session) {
		// je recherche les tickets par ordre decroissant
		model.addAttribute("SqlServer", paramsSqlServerRepos.findParamSqlServer());
		return "sqlserver/index";
	}

	/*
	 * Fonction qui affiche par id
	 */
	public ParamsSqlServer findOne(Long id) {
		ParamsSqlServer sqlserver = null;
		try {
			sqlserver = paramsSqlServerRepos.findSqlServerById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sqlserver;
	}

	/*
	 * Fonction qui affiche la page de modification
	 */
	@GetMapping(value = "/formupdate")
	public String formupdate(Long id, Model model) {
		try {
			model.addAttribute("ParamsSqlServer", findOne(id));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sqlserver/update";
	}

	/*
	 * Fonction qui modification
	 */
	@PostMapping(value = "/update")
	public String update(ParamsSqlServer a, BindingResult bindingResult) {
		ParamsSqlServer paramsSqlServer = null;
		try {
			if (bindingResult.hasErrors()) {
				return "sqlserver/update";
			}

			paramsSqlServer = findOne(a.getId());
			if (paramsSqlServer != null) {
				if (a.getPassword() == "") {
					a.setPassword(paramsSqlServer.getPassword());
				}

				a.setDateModification(new Date());
				a = paramsSqlServerRepos.save(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/sql-server";
	}

}
