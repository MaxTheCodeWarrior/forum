package telran.forumservice.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor

public class Comment {
	@Setter
	String user;
	@Setter
	String message;

	LocalDateTime dateCreated = LocalDateTime.now();

	int likes;

	public void addLike() {
		likes++;
	}

}
