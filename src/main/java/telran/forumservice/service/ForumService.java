package telran.forumservice.service;

import java.util.List;

import telran.forumservice.dto.CommentDto;
import telran.forumservice.dto.PeriodDto;
import telran.forumservice.dto.PostCreateDto;
import telran.forumservice.dto.PostDto;
import telran.forumservice.dto.PostUpdateDto;

public interface ForumService {

	PostDto addPost(String author, PostCreateDto PostCreateDto);

	PostDto findPost(String id);

	void addLike(String id);

	List<PostDto> findPostsByAuthor(String author);

	PostDto addComment(String id, String user, CommentDto commentDto);

	PostDto deletePost(String id);

	List<PostDto> findPostsByTags(List<String> tags);

	List<PostDto> findPostsByPeriod(PeriodDto periodDto);

	PostDto updatePost(String id, PostUpdateDto postUpdateDto);

}
