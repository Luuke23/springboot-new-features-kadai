package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;
@Controller
@RequestMapping("houses/{id}/reviews")
public class ReviewController {
	private final UserRepository userRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;
	private final ReviewRepository reviewRepository;
	
	public ReviewController(UserRepository userRepository, HouseRepository houseRepository, ReviewService reviewService, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;      
        this.houseRepository = houseRepository;
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }  
	
//レビューフォームを表示する
	@GetMapping
	public String review(@PathVariable(name = "id") Integer id,Model model) {
		House house = houseRepository.getReferenceById(id);
		
		model.addAttribute("house", house);
		model.addAttribute("reviewPostForm", new ReviewPostForm());
		return "reviews/review";
	}
//レビューフォームの内容をReviewRepositoryに保存し、民宿詳細ページにリダイレクトする
	@PostMapping("/post")
	public String post(@ModelAttribute @Validated ReviewPostForm reviewPostForm,BindingResult bindingResult,@PathVariable(name = "id") House house_id,@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
            return "reviews/review";
        }
		
		User user_id = userRepository.getReferenceById(userDetailsImpl.getUser().getId()); 
		reviewService.create(reviewPostForm, house_id, user_id);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		
		return "redirect:/houses/{id}";
	}
	
//	レビュー一覧を表示
	@GetMapping("/index")
	public String index(@PathVariable(name = "id") Integer id, Model model, @PageableDefault(page = 0, size = 6, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
		House house = houseRepository.getReferenceById(id);
		
		Page<Review> reviewPage = reviewRepository.findByHouse(house, pageable);
		
		model.addAttribute("house", house);
		model.addAttribute("reviewPage", reviewPage);
		
		return "reviews/index";
	}
	
//	レビュー編集ページを表示
	@GetMapping("/{review_id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, @PathVariable(name = "review_id") Integer review_id, Model model) {
		House house = houseRepository.getReferenceById(id);
		Review review = reviewRepository.getReferenceById(review_id);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(),review.getScore(),review.getDescription());
		
		
		model.addAttribute("house", house);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm",reviewEditForm);
		return "reviews/edit";
	}
	
//	レビューを更新
	@PostMapping("/{review_id}/update")
	public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
            return "reviews/edit";
        }
		
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/houses/{id}";
	}
	
//	レビューを削除
	@PostMapping("/{review_id}/delete") 
	public String delete (@PathVariable(name = "review_id") Integer review_id, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(review_id);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/houses/{id}";
	}
}
