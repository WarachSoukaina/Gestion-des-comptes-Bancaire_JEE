package org.warach.metier;

import org.springframework.data.domain.Page;
import org.warach.entities.Compte;
import org.warach.entities.Operation;

public interface IBanqueMetier {
	// les composants de Use cases
	public Compte consulterCompte(String codeCpte);
	public void verser(String CodeCpte,double montant);
	public void retirer(String CodeCpte,double montant);
	public void virement(String CodeCpte1,String CodeCpte2,double montant);
	public Page<Operation> listOperation(String CodeCpte, int page, int size );
	
	
}
