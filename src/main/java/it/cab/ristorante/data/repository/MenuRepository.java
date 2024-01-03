package it.cab.ristorante.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cab.ristorante.data.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

}
