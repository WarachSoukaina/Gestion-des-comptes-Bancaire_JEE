package org.warach.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.warach.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

}
