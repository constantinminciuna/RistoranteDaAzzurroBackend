package it.cab.ristorante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.cab.ristorante.data.model.Menu;
import it.cab.ristorante.data.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor	
public class MenuService {
	
	private final MenuRepository menuRepository;
	
	public List<Menu> getMenus(){
		return menuRepository.findAll();
	}
	
	public Menu getMenu(Long id) {
		return menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu non trovato con ID: " + id));
	}
	
	public Menu creaMenu(Menu menu) {
		return menuRepository.save(menu);
	}
	
	public Menu aggiornaMenu(Long id, Menu menuDetails) {
		Menu menu = getMenu(id);
		
		menu.setNome(menuDetails.getNome());
		menu.setCategoria(menuDetails.getCategoria());
		menu.setDescrizione(menuDetails.getDescrizione());
		menu.setPrezzo(menuDetails.getPrezzo());
		
		return menuRepository.save(menu);
	
	}
	
	public void cancellaMenu(Long id) {
		Menu menu = getMenu(id);
		menuRepository.delete(menu);
	}
	
}
