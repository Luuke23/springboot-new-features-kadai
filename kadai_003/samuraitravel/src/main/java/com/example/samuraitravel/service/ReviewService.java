package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.ReviewRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
//	投稿したレビューをリポジトリに保存する
	@Transactional
	public Review create(ReviewPostForm reviewPostForm, House house_id, User user_id) {
		Review review = new Review();
		
		review.setHouse(house_id);
		review.setUser(user_id);
		review.setScore(reviewPostForm.getScore());
		review.setDescription(reviewPostForm.getDescription());
		
		return reviewRepository.save(review);
	}
	
	@Transactional
	public Review update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setScore(reviewEditForm.getScore());
		review.setDescription(reviewEditForm.getDescription());
		
		return reviewRepository.save(review);
	}

}
