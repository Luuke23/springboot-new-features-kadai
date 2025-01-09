package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewPostForm {

	@NotNull(message = "評価を選んでください。")
	private Integer score;

	@NotBlank(message = "コメントを入力してください")
	private String description;

}
