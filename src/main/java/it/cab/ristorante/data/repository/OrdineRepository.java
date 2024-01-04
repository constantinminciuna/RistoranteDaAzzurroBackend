package it.cab.ristorante.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cab.ristorante.data.model.Ordine;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
	
	Optional<List<Ordine>> findByIdUtente(Integer idUtente);
	
	Page<Ordine> findByIdUtente(Integer idUtente, Pageable pageable);
	
}
