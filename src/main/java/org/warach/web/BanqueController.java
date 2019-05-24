package org.warach.web;

import javax.validation.Valid;

import org.warach.entities.Client;
import org.warach.dao.ClientRepository;
import org.warach.dao.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.warach.entities.Compte;
import org.warach.entities.Operation;
import org.warach.metier.IBanqueMetier;

@Controller
public class BanqueController {
	@Autowired
	private IBanqueMetier banqueMetier;

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@RequestMapping("/operations")
	public String index() {
		
		return "comptes";
		
	}
	
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model, String codeCompte, @RequestParam(name="page",defaultValue="0")int page,@RequestParam(name="size",defaultValue="5")int size ) {
		model.addAttribute("codeCompte", codeCompte);
		try {
			Compte cp=banqueMetier.consulterCompte(codeCompte);
			Page<Operation> pageOperations=banqueMetier.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperations.getContent());
			int[] pages=new int[pageOperations.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("compte",cp);
		} catch (Exception e) {
			model.addAttribute("exception", e);
		}
		
		return "comptes";
		
	}
	
	@RequestMapping(value="/saveOperation",method=RequestMethod.POST)   
	public String saveOperation(Model model,String typeOperation, String codeCompte,double montant, String codeCompte2) {   
		
		try {
			if(typeOperation.equals("VERS")) {
				
				banqueMetier.verser(codeCompte,montant);
			} else if(typeOperation.equals("RETR")) {
				
				banqueMetier.retirer(codeCompte,montant);
			} else  {
				
				banqueMetier.virement(codeCompte,codeCompte2,montant);
			} 
		}catch (Exception e) {
			model.addAttribute("error",e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error="+e.getMessage();  
		}
		 
		
		return "redirect:/consulterCompte?codeCompte="+codeCompte;  
	}
	
	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String formClient(Model model) {
		model.addAttribute("client", new Client());
		return "FormClient";
	}

	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(Model model,@Valid Client client,BindingResult result){
	if(result.hasErrors())
		return "FormClient";
	clientRepository.save(client);
	return "Comptes";
	}
	
	// récupperer les informations des comptes : 
	
	/*
		@RequestMapping(value="/comptes", method=RequestMethod.GET)
		public String getComptes(Model model){
			
			List<Compte> ListComptes=compteRepository.listComptes(5);
			  model.addAttribute("ListComptes",ListComptes) ;
			return "ListComptes"; // html a cree
		}
		
		// récupperer les informations des comptes : 
				@RequestMapping(value="/clients", method=RequestMethod.GET)
				public List<Client> getClients(){
					return clientRepository.findAll();
				} */

}
