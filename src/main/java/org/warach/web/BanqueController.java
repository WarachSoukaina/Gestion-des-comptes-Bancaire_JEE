package org.warach.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.warach.entities.Compte;
import org.warach.entities.Operation;
import org.warach.metier.IBanqueMetier;

@Controller
public class BanqueController {
	@Autowired
	private IBanqueMetier banqueMetier;
	
	@RequestMapping("/operations")
	public String index() {
		
		return "comptes";
		
	}
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model, String codeCompte) {
		model.addAttribute("codeCompte", codeCompte);
		
		try {
			Compte cp = banqueMetier.consuleterCompte(codeCompte);
			Page<Operation> pageOperations = banqueMetier.listOperation(codeCompte, 0, 5);
			model.addAttribute("compte", cp);
			model.addAttribute("listOperations",pageOperations.getContent());
		} catch (Exception e) {
			// TODO: handle exception
			
			model.addAttribute("exception", e);
		}
		
		return "comptes";
		
	}
	

}
