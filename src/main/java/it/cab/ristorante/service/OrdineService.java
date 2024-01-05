 package it.cab.ristorante.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.cab.ristorante.data.model.Ordine;
import it.cab.ristorante.data.model.OrdineMenu;
import it.cab.ristorante.data.repository.OrdineMenuRepository;
import it.cab.ristorante.data.repository.OrdineRepository;
import it.cab.ristorante.exception.ConsegnaNonRiuscitaException;
import it.cab.ristorante.exception.OrdineGiaAccettatoException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdineService {
	
	private final OrdineRepository ordineRepository;
	private final OrdineMenuRepository ordineMenuRepository;
	
//	private Queue<Ordine> ordiniAccettatiInCoda = new LinkedList<>();
	
	public List<Ordine> getOrdini(){
		return ordineRepository.findAll();
	}
	
	public Ordine getOrdine(Long id) {
		return ordineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con ID: " + id));
	}
	
	public List<Ordine> getOrdineByUtente(Integer id) {
		return ordineRepository.findByIdUtente(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordini non trovato con ID utente: " + id));
	}
	
	public Ordine creaOrdine(Ordine ordine) {
		List<OrdineMenu> ordMen = ordine.getOrdineMenu();
		
		double prezzo = 0;
		
		for (OrdineMenu ordineMenu : ordMen) {
			prezzo = prezzo + (ordineMenu.getMenu().getPrezzo() * ordineMenu.getQuantita());
		}
		
		Ordine ordineTemp = ordineRepository.save(ordine);
		
		for (OrdineMenu ordineMenu : ordMen) {
			ordineMenu.setOrdine(ordineTemp);
			ordineMenuRepository.save(ordineMenu);
		}
		
		return ordineTemp;
	}
	
	public Ordine aggiornaOrdine(Long id, Ordine ordineDetails) {
		Ordine ordine = getOrdine(id);
		
		ordine.setOrdineMenu(ordineDetails.getOrdineMenu());
		ordine.setIndirizzo(ordineDetails.getIndirizzo());
		ordine.setData(ordineDetails.getData());
		ordine.setPrezzo(ordineDetails.getPrezzo());
		ordine.setAccettato(ordineDetails.isAccettato());
		ordine.setConsegnato(ordineDetails.isConsegnato());
		
		return ordineRepository.save(ordine);
	
	}
	
	public void cancellaOrdine(Long id) {
		Ordine ordine = getOrdine(id);
		ordineRepository.delete(ordine);
	}
	
	public Ordine accettaOrdine(Long idOrdine) {
        Ordine ordine = getOrdine(idOrdine);

        if (!ordine.isAccettato()) {
        	
        	while(ordine.isAccettato() == false) {
        		List<Ordine> ordineOrarioCheck = ordineRepository.findByData(ordine.getData());
        		
        		if(ordineOrarioCheck.isEmpty()) {
            		ordine.setAccettato(true);
                    ordine = ordineRepository.save(ordine);
            	} else {
            		ordine.setData(ordine.getData().plusHours(1));
            	}
        	}
        	
        	consegnaOrdine(ordine);
            
        	/*
            try {
				Thread.sleep(20000);
				
				consegnaOrdine(ordine);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
            
            return ordine;
        } else {
            throw new OrdineGiaAccettatoException(idOrdine);
        }
    }
    
	private void consegnaOrdine(Ordine ordine) {
        if (ordine != null && !ordine.isConsegnato()) {
            ordine.setConsegnato(true);
            ordineRepository.save(ordine);
        } else throw new ConsegnaNonRiuscitaException();
    }
	
	public Map<String, Object> getOrdiniListByIdUtente(Integer id, int page, int size){
		try {
			List<Ordine> ordini = new ArrayList<Ordine>();
			Pageable paging = PageRequest.of(page, size);
	
			Page<Ordine> pageOrdini = ordineRepository.findByIdUtente(id, paging);
			
			ordini = pageOrdini.getContent();
	
			Map<String, Object> response = new HashMap<>();
			response.put("ordini", ordini);
			response.put("currentPage", pageOrdini.getNumber());
			response.put("totalItems", pageOrdini.getTotalElements());
			response.put("totalPages", pageOrdini.getTotalPages());
			
			return response;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Map<String, Object> getOrdiniList(int page, int size){
		try {
			List<Ordine> ordini = new ArrayList<Ordine>();
			Pageable paging = PageRequest.of(page, size);
	
			Page<Ordine> pageOrdini = ordineRepository.findAll(paging);
			
			ordini = pageOrdini.getContent();
	
			Map<String, Object> response = new HashMap<>();
			response.put("ordini", ordini);
			response.put("currentPage", pageOrdini.getNumber());
			response.put("totalItems", pageOrdini.getTotalElements());
			response.put("totalPages", pageOrdini.getTotalPages());
			
			return response;
		} catch (Exception e) {
			return null;
		}
	}
	
}
