package telran.forumservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.forumservice.dto.CommentDto;
import telran.forumservice.dto.PeriodDto;
import telran.forumservice.dto.PostCreateDto;
import telran.forumservice.dto.PostDto;
import telran.forumservice.dto.PostUpdateDto;
import telran.forumservice.service.ForumService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

	final ForumService forumService;
	final ModelMapper modelMapper;

	@PostMapping("/post/{user}")
	public ResponseEntity<PostDto> addPost(@PathVariable("user") String author,
			@RequestBody PostCreateDto postCreateDto) {

		PostDto postDto = forumService.addPost(author, postCreateDto);

		if (postDto != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postDto);
		}
	}

	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> findPost(@PathVariable("postId") String id) {
		PostDto postDto = forumService.findPost(id);
		return ResponseEntity.status(postDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(postDto);
	}

	@PutMapping("/post/{postId}/like")
	public ResponseEntity<Void> addLike(@PathVariable("postId") String id) {
		forumService.addLike(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/posts/author/{user}")
	public ResponseEntity<List<PostDto>> findPostsByAuthor(@PathVariable("user") String author) {	
		List<PostDto>  postsByAuthor = forumService.findPostsByAuthor(author);
		if(postsByAuthor.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<PostDto>());
		}
		return  ResponseEntity.status(HttpStatus.OK).body(postsByAuthor);
	}

	@PutMapping("/post/{postId}/comment/{user}")
	public ResponseEntity<PostDto> addComment(@PathVariable("postId") String id, @PathVariable("user") String author,
			@RequestBody CommentDto commentDto) {
		PostDto postDto = forumService.addComment(id, author, commentDto);
		return ResponseEntity.status(postDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(postDto);
	}

	@DeleteMapping("/post/{postId}")
	public ResponseEntity<PostDto> deletePost(@PathVariable("postId") String id) {
		PostDto postDto = forumService.deletePost(id);
		return ResponseEntity.status(postDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(postDto);
	}

	@PostMapping("/posts/tags")
	public ResponseEntity<List<PostDto>> findPostsByTags(@RequestBody List<String> tags) {
		List<PostDto> postsByTags = forumService.findPostsByTags(tags);
		return ResponseEntity.status(postsByTags.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(postsByTags);
	}

	@PostMapping("/posts/period")
	public ResponseEntity<List<PostDto>> findPostsByPeriod(@RequestBody PeriodDto periodDto) {

		List<PostDto> postsByPeriod = forumService.findPostsByPeriod(periodDto);
		return ResponseEntity.status(postsByPeriod.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK)
				.body(postsByPeriod);
	}

	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> updatePost(@PathVariable("postId") String id,
			@RequestBody PostUpdateDto postUpdateDto) {
		PostDto updatedPostDto = forumService.updatePost(id, postUpdateDto);
		return ResponseEntity.status(updatedPostDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND)
				.body(updatedPostDto);
	}
}
