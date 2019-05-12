package org.warach.metier;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.warach.dao.CompteRepository;
import org.warach.dao.OperationRepository;
import org.warach.entities.Compte;
import org.warach.entities.CompteCourant;
import org.warach.entities.Operation;
import org.warach.entities.Retrait;
import org.warach.entities.Versement;

@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier {
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository  operationRepository;
	@Override
	public Compte consuleterCompte(String codeCpte) {
	
		Compte cp = compteRepository.findById(codeCpte).orElse(null);
		if(cp==null) throw new RuntimeException("Compte introvable");
		return cp;
	}

	@Override
	public void verser(String CodeCpte, double montant) {
		
		Compte cp =	consuleterCompte(CodeCpte);
		Versement v = new Versement(new Date(), montant, cp);
		operationRepository.save(v);// enregister l'operation de versement 
		cp.setSolde(cp.getSolde()+montant);// mise a jour du solde 
		compteRepository.save(cp); // enregister les modification faites sur le compte 
		
	}

	@Override
	public void retirer(String CodeCpte, double montant) {
		
		Compte cp =	consuleterCompte(CodeCpte);
		double facilitesCaisse=0;
		
		if(cp instanceof CompteCourant )
			facilitesCaisse=((CompteCourant) cp).getDecouvert();
		if(cp.getSolde()+facilitesCaisse<montant) throw new RuntimeException("Solde insuffaisant");
		
		Retrait r = new Retrait(new Date(), montant, cp);
		operationRepository.save(r);// enregister l'operation de versement 
		cp.setSolde(cp.getSolde()-montant);// mise a jour du solde 
		compteRepository.save(cp); // enregister les modification faites sur le compte 
	
		
	}

	@Override
	public void virement(String CodeCpte1, String CodeCpte2, double montant) {
		retirer(CodeCpte1,montant);
		verser(CodeCpte2, montant);
		
		
	}

	@Override
	public Page<Operation> listOperation(String CodeCpte, int page, int size) {
	
		return operationRepository.listOperation(CodeCpte, new PageRequest(page, size) );
	}

}
