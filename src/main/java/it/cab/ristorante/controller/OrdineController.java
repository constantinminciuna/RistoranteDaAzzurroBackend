package it.cab.ristorante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.cab.ristorante.data.model.Ordine;
import it.cab.ristorante.service.OrdineService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ordine")
@RequiredArgsConstructor
public class OrdineController {
	
	private final OrdineService ordineService;
	
	@GetMapping
	public List<Ordine> getOrdini() {
		return ordineService.getOrdini();
	}
	
	@GetMapping("/{id}")
	public Ordine getOrdine(@PathVariable(value = "id") Long id) {
		return ordineService.getOrdine(id);
	}
	
	@GetMapping("/list/{id}")
	public List<Ordine> getOrdiniByUtente(@PathVariable(value = "id") Integer id) {
		return ordineService.getOrdineByUtente(id);
	}
	
	@PostMapping(consumes="application/json")
	public Ordine creaOrdine(@Validated @RequestBody Ordine ordine) {
		return ordineService.creaOrdine(ordine);
	}
	
	@PutMapping("/{id}")
	public Ordine updateOrdine(@PathVariable(value = "id") Long id, @Validated @RequestBody Ordine ordineDetails) {
		return ordineService.aggiornaOrdine(id, ordineDetails);	
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrdine(@PathVariable(value = "id") Long id) {
		ordineService.cancellaOrdine(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/accetta")
	public ResponseEntity<Ordine> accettaOrdine(@Validated @RequestBody Long id) {
		Ordine ordine = ordineService.accettaOrdine(id);
		return new ResponseEntity<>(ordine, HttpStatus.OK);
	}

}
