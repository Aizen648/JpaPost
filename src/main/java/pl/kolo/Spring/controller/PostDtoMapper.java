package pl.kolo.Spring.controller;

import pl.kolo.Spring.controller.dto.PostDto;
import pl.kolo.Spring.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostDtoMapper {
    private PostDtoMapper(){

    }
    public static List<PostDto> mapToPostDtos(List<Post> postPage) {
        return postPage.stream().map(post -> mapToPostDtos(post))
                .collect(Collectors.toList());
    }

    private static PostDto mapToPostDtos(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .created(post.getCreated())
                .build();
    }

}
