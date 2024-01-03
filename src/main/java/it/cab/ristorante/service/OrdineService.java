 package it.cab.ristorante.service;

import java.util.List;

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
                .orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con ID: " + id));
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
            ordine.setAccettato(true);
            
            ordine = ordineRepository.save(ordine);
            
            //ordiniAccettatiInCoda.add(ordine);
            
            try {
				Thread.sleep(20000);
				
				consegnaOrdine(ordine);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
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
	
//	//@Scheduled(cron = "0 0 19,20,21,22,23 ? * *")
//	@Scheduled(cron = "0 * 0 ? * *")
//    public void consegnaOrdiniInCoda() {
//		while (!ordiniAccettatiInCoda.isEmpty()) {
//            Ordine ordineDaConsegnare = ordiniAccettatiInCoda.poll();
//            consegnaOrdine(ordineDaConsegnare);
//        }
//    }

}
