package pl.kolo.Spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kolo.Spring.model.Comment;
import pl.kolo.Spring.model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPostIdIn(List<Long> ids);
}
