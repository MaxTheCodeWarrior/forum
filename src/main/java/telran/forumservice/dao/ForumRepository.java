package telran.forumservice.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.forumservice.model.Post;

public interface ForumRepository extends CrudRepository<Post, String> {


	Stream<Post> findPostsByAuthor(String author);
    Stream<Post> findByDateCreatedBetween(LocalDateTime dateCreatedFrom, LocalDateTime dateCreatedTo);
    Stream<Post> findByTagsIn(List<String> tags);


}
