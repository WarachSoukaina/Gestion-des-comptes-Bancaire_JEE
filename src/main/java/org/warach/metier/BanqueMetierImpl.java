package org.warach.metier;


import java.util.Date;

import org.warach.dao.CompteRepository;
import org.warach.dao.OperationRepository;
import org.warach.entities.Compte;
import org.warach.entities.CompteCourant;
import org.warach.entities.Operation;
import org.warach.entities.Retrait;
import org.warach.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier {
	
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Override
	
	public Compte consulterCompte(String codeCpte) {
		Compte cp=compteRepository.findById(codeCpte).orElse(null);
		if (cp==null) throw new RuntimeException("Compte introuvable");
		return cp;
	}

	@Override
	public void verser(String codeCpte, double montant) {
		Compte cp = consulterCompte(codeCpte);
		Versement v = new Versement(new Date(), montant,cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		Compte cp = consulterCompte(codeCpte);
		double facilitiesCaisse=0;
		if(cp instanceof CompteCourant)
			facilitiesCaisse=((CompteCourant) cp).getDecouvert();
		if(cp.getSolde()+facilitiesCaisse<montant)
			throw new RuntimeException("Solde insuffisant");
		Retrait r = new Retrait(new Date(), montant,cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()-montant);
		compteRepository.save(cp);
		
	}

	@Override
	public void virement(String codeCpte1, String codeCpte2, double montant) {
		if(codeCpte1.equals(codeCpte2))
			throw new RuntimeException("Impossible d'effectuer un virement dans le meme compte");
		retirer(codeCpte1, montant);
		verser(codeCpte2, montant);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {
		
		return operationRepository.listOperation(codeCpte, new PageRequest(page, size ));
	}

}
