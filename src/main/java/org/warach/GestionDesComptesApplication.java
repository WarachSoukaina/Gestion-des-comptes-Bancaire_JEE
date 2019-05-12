package org.warach;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.warach.dao.ClientRepository;
import org.warach.dao.CompteRepository;
import org.warach.dao.OperationRepository;
import org.warach.entities.Client;
import org.warach.entities.Compte;
import org.warach.entities.CompteCourant;
import org.warach.entities.CompteEpargne;
import org.warach.entities.Retrait;
import org.warach.entities.Versement;
import org.warach.metier.IBanqueMetier;

@SpringBootApplication
public class GestionDesComptesApplication implements CommandLineRunner {
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private IBanqueMetier banqueMetier;
	
	public static void main(String[] args) {
		SpringApplication.run(GestionDesComptesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		
		Client c1= clientRepository.save(new Client("Soukaina", "soukaina@gmail.com"));
		Client c2= clientRepository.save(new Client("fati", "fati@gmail.com"));
		
		Compte cp1 = compteRepository.save(new CompteCourant("c1", new Date(), 90000, c1, 6000));
		Compte cp2 = compteRepository.save(new CompteEpargne("c2", new Date(), 6000, c2, 5.5));
		operationRepository.save(new Versement(new Date(), 9000, cp1));
		operationRepository.save(new Versement(new Date(), 6000, cp1));
		operationRepository.save(new Versement(new Date(), 2000, cp1));
		operationRepository.save(new Retrait(new Date(), 6000, cp1));
		
		operationRepository.save(new Versement(new Date(), 9000, cp2));
		operationRepository.save(new Versement(new Date(), 6000, cp2));
		operationRepository.save(new Versement(new Date(), 2000, cp2));
		operationRepository.save(new Retrait(new Date(), 6000, cp2));
		banqueMetier.verser("c1", 10000);// tester la couche métier
		
	}
	
	

}
