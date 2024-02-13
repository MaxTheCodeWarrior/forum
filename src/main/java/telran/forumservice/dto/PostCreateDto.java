package telran.forumservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostCreateDto {
	@Setter
	String author;
	@Setter
	String title;
	@Setter
	String content;
	@Setter
	List<String> tags;
}
