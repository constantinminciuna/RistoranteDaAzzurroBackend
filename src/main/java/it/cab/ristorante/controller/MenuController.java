package it.cab.ristorante.controller;

import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.cab.ristorante.data.model.Menu;
import it.cab.ristorante.service.MenuService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@JsonIgnoreProperties("menu")
public class MenuController {
	
	private final MenuService menuService;
	
	@GetMapping
	public List<Menu> getMenus() {
		return menuService.getMenus();
	}
	
	@GetMapping("/admin/{id}")
	public Menu getMenu(@PathVariable(value = "id")  Long id) {
		return menuService.getMenu(id);
	}
	
	@PostMapping(consumes="application/json", path = "/admin")
	public Menu creaMenu(@Validated @RequestBody Menu menu) {
		return menuService.creaMenu(menu);
		
	}
	
	@PutMapping("/admin/{id}")
	public Menu updateMenu(@PathVariable(value = "id") Long id, @Validated @RequestBody Menu menuDetails) {
		return menuService.aggiornaMenu(id, menuDetails);	
	}

	@DeleteMapping("/admin/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable(value = "id") Long id) {
		menuService.cancellaMenu(id);
		return ResponseEntity.ok().build();
	}
	
}
