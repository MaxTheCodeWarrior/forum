package telran.forumservice.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor

public class Post {

	@Id
	String id;
	@Setter
	String title;
	@Setter
	String content;
	@Setter
	String author;
	@Setter
	LocalDateTime dateCreated;

	@Setter
	List<String> tags;

	int likes;

	
	List<Comment> comments;

	public void addLike() {
		this.likes++;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}
}
