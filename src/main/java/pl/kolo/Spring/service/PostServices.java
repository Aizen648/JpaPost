package pl.kolo.Spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.kolo.Spring.model.Comment;
import pl.kolo.Spring.model.Post;
import pl.kolo.Spring.repository.CommentRepository;
import pl.kolo.Spring.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServices {
    private static final int PAGE_SIZE = 20;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Post> getPost() {
        return postRepository.findAll();
    }

    public List<Post> getPostPage(int page) {
        return postRepository.findAllPosts(PageRequest.of(page, PAGE_SIZE));
    }

    public List<Post> getPostPageSort(int page, Sort.Direction sort) {
        return postRepository.findAllPosts(PageRequest.of(page, PAGE_SIZE, Sort.by(sort, "id")));
    }

    public List<Post> getPostPageWithComments(int page) {
        List<Post> allPosts = postRepository.findAllPosts(PageRequest.of(page, PAGE_SIZE));
        List<Long> ids = allPosts.stream()
                .map(Post::getId)
                .toList();
        List<Comment> comments = commentRepository.findAllByPostIdIn(ids);
        allPosts.forEach(post -> post.setComments(extractComment(comments, post.getId())));
        return allPosts;
    }

    public List<Post> getPostPageWithCommentsSorted(int page, Sort.Direction sort) {
        List<Post> allPosts = postRepository.findAllPosts(PageRequest.of(page, PAGE_SIZE, Sort.by(sort, "id")));
        List<Long> ids = allPosts.stream()
                .map(Post::getId)
                .toList();
        List<Comment> comments = commentRepository.findAllByPostIdIn(ids);
        allPosts.forEach(post -> post.setComments(extractComment(comments, post.getId())));
        return allPosts;
    }

    private List<Comment> extractComment(List<Comment> comments, long id) {
        return comments.stream()
                .filter(comment -> comment.getPostId() == id).toList();
    }


    public Post getSinglePost(long id) {
        return postRepository.findById(id).orElseThrow();
    }

    public List<Post> getByTitle(String title) {
        return postRepository.findAllByTitle(title);
    }

    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post editPost(Post post) {
        Post byId = postRepository.findById(post.getId()).orElseThrow();
        byId.setTitle(post.getTitle());
        byId.setContent(post.getContent());
        return byId;
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }
}