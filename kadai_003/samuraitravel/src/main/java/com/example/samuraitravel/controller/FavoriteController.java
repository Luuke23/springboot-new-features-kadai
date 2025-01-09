package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller
public class FavoriteController {
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	private final HouseRepository houseRepository;
	
	public FavoriteController(UserRepository userRepository, FavoriteRepository favoriteRepository, FavoriteService favoriteService, HouseRepository houseRepository) {
		this.userRepository = userRepository;
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
		this.houseRepository = houseRepository;
	}
	
//	お気に入り一覧を表示
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId()); 
		Page<Favorite> favoritePage = favoriteRepository.findByUser(user, pageable);
		
		model.addAttribute("favoritePage", favoritePage);
		return "favorites/index";
	}

// お気に入り追加
	@PostMapping("/houses/{house_id}/favorites/create")
	public String create(@PathVariable(name = "house_id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
		House house = houseRepository.getReferenceById(id);
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
	
		favoriteService.create(house, user);
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
		return "redirect:/houses/{house_id}";
	}
	
//	お気に入り削除
	@PostMapping("/houses/{house_id}/favorites/{favorite_id}/delete")
	public String delete(@PathVariable(name = "favorite_id") Integer id,RedirectAttributes redirectAttributes) {
		favoriteRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
		return "redirect:/houses/{house_id}";
	}
}
