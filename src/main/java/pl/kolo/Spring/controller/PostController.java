package pl.kolo.Spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.kolo.Spring.controller.dto.PostDto;
import pl.kolo.Spring.exception.MockException;
import pl.kolo.Spring.model.Post;
import pl.kolo.Spring.service.PostServices;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServices postServices;

    @GetMapping("/hi")
    public String getHi(){
        return "hello kolo ;D";
    }
    @GetMapping("/postsAll")
    public List<Post> getAllPosts(){
        return postServices.getPost();
    }
    @GetMapping("/posts") //with comment
    public List<Post> getPostsPage(@RequestParam(required = false) Integer page){
        int pageNumber = page != null && page > 0 ? page : 1;
        return postServices.getPostPageWithComments(pageNumber-1);
    }
    @GetMapping("/postsSorted") //with comment
    public List<Post> getPostsPageSorted(@RequestParam(required = false) Integer page,Sort.Direction sort){
        int pageNumber = page != null && page > 0 ? page : 1;
        Sort.Direction sortDirection = sort != null ? sort: Sort.Direction.ASC;
        return postServices.getPostPageWithCommentsSorted(pageNumber-1,sortDirection);
    }
    @GetMapping("/postsNoComment") //without comment
    public List<PostDto> getPostsPageNoComment(@RequestParam(required = false) Integer page){
        int pageNumber = page != null && page > 0 ? page : 1;
        return PostDtoMapper.mapToPostDtos(postServices.getPostPage(pageNumber-1));
    }
    @GetMapping("/postsNoCommentSorted")
    public List<PostDto> getPostsPageNoCommentSort(@RequestParam(required = false) Integer page, Sort.Direction sort){
        int pageNumber = page != null && page > 0 ? page : 1;
        Sort.Direction sortDirection = sort != null ? sort: Sort.Direction.ASC;
        return PostDtoMapper.mapToPostDtos(postServices.getPostPageSort(pageNumber-1, sortDirection));
    }
    @GetMapping("/posts/{id}")
    public Post getSinglePost(@PathVariable long id){
        return postServices.getSinglePost(id);
    }
    @GetMapping("/posts/title/{title}")
    public List<Post> getSinglePostByTitle(@PathVariable String title){
        return postServices.getByTitle(title);
    }
    @GetMapping("/exceptions")
    public List<?> notImplemented(){
        throw new MockException(12);
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody Post post){
        return postServices.addPost(post);
    }

    @PutMapping("/posts")
    public Post editPost(@RequestBody Post post){
        return postServices.editPost(post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable long id){
        postServices.deletePost(id);
    }
}
