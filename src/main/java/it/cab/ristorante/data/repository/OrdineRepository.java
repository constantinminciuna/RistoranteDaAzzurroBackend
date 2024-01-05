package it.cab.ristorante.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cab.ristorante.data.model.Ordine;


@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
	
	Optional<List<Ordine>> findByIdUtente(Integer idUtente);
	
	Page<Ordine> findByIdUtente(Integer idUtente, Pageable pageable);
	
	@Query(value = "select * from ordine o where o.data = :data and o.accettato = 1",
			nativeQuery = true)
	List<Ordine> findByData(@Param("data") LocalDateTime data);
	
}
