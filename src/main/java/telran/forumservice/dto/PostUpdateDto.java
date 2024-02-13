package telran.forumservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class PostUpdateDto {
	@Setter
	String title;
	@Setter
	String content;
	@Setter
	List<String> tags;
	// maybe new LocalDateTime;
}
