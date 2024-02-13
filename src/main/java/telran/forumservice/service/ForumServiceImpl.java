package telran.forumservice.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.forumservice.dao.ForumRepository;
import telran.forumservice.dto.CommentDto;
import telran.forumservice.dto.PeriodDto;
import telran.forumservice.dto.PostCreateDto;
import telran.forumservice.dto.PostDto;
import telran.forumservice.dto.PostUpdateDto;
import telran.forumservice.dto.exceptions.PostNotFoundException;
import telran.forumservice.model.Comment;
import telran.forumservice.model.Post;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {

	final ForumRepository forumRepository;
	final ModelMapper modelMapper;

	@Override
	public PostDto addPost(String author, PostCreateDto postCreateDto) {
		PostDto postDto = modelMapper.map(postCreateDto, PostDto.class);
		postDto.setAuthor(author);
		Post post = modelMapper.map(postDto, Post.class);
		post.setDateCreated(LocalDateTime.now());
		forumRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto findPost(String id) {
		Post post = forumRepository.findById(id).orElseThrow(PostNotFoundException::new);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public void addLike(String id) {
		Post post = forumRepository.findById(id).orElseThrow(PostNotFoundException::new);
		post.addLike();
		forumRepository.save(post);
	}

	@Override
	public List<PostDto> findPostsByAuthor(String author) {
		return forumRepository.findPostsByAuthor(author).map(p -> modelMapper.map(p, PostDto.class)).toList();
	}

	@Override
	public PostDto addComment(String id, String user, CommentDto commentDto) {
		Post post = forumRepository.findById(id).orElseThrow(PostNotFoundException::new);
		Comment newComment = new Comment();

		if (commentDto.getMessage() != null) {
			newComment.setMessage(commentDto.getMessage());
		}
		newComment.setUser(user);

		post.addComment(newComment);

		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto deletePost(String id) {
		Post post = forumRepository.findById(id).orElseThrow(PostNotFoundException::new);
		forumRepository.delete(post);

		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> findPostsByTags(List<String> tags) {
		return forumRepository.findByTagsIn(tags).map(p -> modelMapper.map(p, PostDto.class)).toList();
	}

	@Override
	public List<PostDto> findPostsByPeriod(PeriodDto periodDto) {
		LocalDateTime dateFrom = LocalDateTime.of(periodDto.getDateFrom(), LocalTime.MIN);
		LocalDateTime dateTo = LocalDateTime.of(periodDto.getDateTo(), LocalTime.MAX);
		return forumRepository.findByDateCreatedBetween(dateFrom, dateTo).map(p -> modelMapper.map(p, PostDto.class))
				.toList();
	}

	@Override
	public PostDto updatePost(String id, PostUpdateDto postUpdateDto) {
		Post post = forumRepository.findById(id).orElseThrow(PostNotFoundException::new);
		post.setContent(postUpdateDto.getContent());
		post.setTitle(postUpdateDto.getTitle());
		post.setTags(postUpdateDto.getTags());
		forumRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

}
