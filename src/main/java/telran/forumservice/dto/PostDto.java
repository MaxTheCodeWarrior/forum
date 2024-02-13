package telran.forumservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.forumservice.model.Comment;


@Getter

@NoArgsConstructor
public class PostDto {

	String id;
	@Setter
	String title;
	@Setter
	String content;
	@Setter
	String author;
	
	LocalDateTime dateCreated;
	@Setter
	List<String> tags;
	
	
	int likes;
	
	@Setter
	List<Comment> comments = new ArrayList<Comment>();;
	
}
