package pl.kolo.Spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kolo.Spring.model.Post;
import pl.kolo.Spring.repository.PostRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    void GetSinglePost () throws Exception {
        //given
        String title="title test";
        String content="content test";
        LocalDateTime created=LocalDateTime.now();

        Post newPost= new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setCreated(created);
        postRepository.save(newPost);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/posts/"+newPost.getId()))
             //   .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        //then
        Post post= objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post.class);
        assertNotNull(post);
        assertEquals(post.getId(),newPost.getId());
        assertEquals(post.getContent(),content);
        assertEquals(post.getCreated(),created);
    }
    @Test
    void getSingleTitle () throws Exception {
        String title="title test";
        String content="content test";
        LocalDateTime created=LocalDateTime.now();

        Post newPost= new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setCreated(created);
        postRepository.save(newPost);


         mockMvc.perform(MockMvcRequestBuilders.get("/posts/title/" + title))
            //    .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is(title)));
    }
    @Test
    void getSingleException () throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/exceptions"))
             //   .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(405))
                .andExpect(MockMvcResultMatchers.content()
                        .string("error number 12 Not implemented yet, and will never be !!!"));
    }
    @Test
    void getSingleHello () throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/hi"))
             //   .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .string("hello kolo ;D"));
    }
    @Test
    void getAllPost () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsAll"))
               // .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        assertTrue(posts.length>90 && posts.length<110);
    }
    @Test
    void getNoCommentSortedASC () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsNoCommentSorted?sort=ASC"))
                // .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        assertTrue( posts.length<21);
        long min=0;
        for (Post post:posts){
            if (post.getId()>min && post.getComments()==null) min=post.getId();
                else fail();
        }
    }
    @Test
    void getNoCommentSortedASCPage2 () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsNoCommentSorted?page=2&sort=ASC"))
                // .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        assertTrue( posts.length<21);
        long min=0;
        for (Post post:posts){
            if (post.getId()>min && post.getComments()==null) min=post.getId();
            else fail();
        }
    }
    @Test
    void getNoCommentSortedDESC () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsNoCommentSorted?sort=DESC"))
                // .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        assertTrue( posts.length<21);
        long max=Long.MAX_VALUE;
        for (Post post:posts){
            if (post.getId()<max && post.getComments()==null) max=post.getId();
            else fail();
        }
    }
    @Test
    void getNoCommentSortedDESCPage2 () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsNoCommentSorted?page=2&sort=DESC"))
                // .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        assertTrue( posts.length<21);
        long max=Long.MAX_VALUE;
        for (Post post:posts){
            if (post.getId()<max && post.getComments()==null) max=post.getId();
            else fail();
        }
    }
    @Test
    void getNoComment () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsNoComment"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        for (Post post:posts){
            if(post.getComments()!=null) fail();
        }
    }

    @Test
    void getPostsSortedASC () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsSorted?page=1&sort=ASC"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        long min=0;
        for (Post post:posts){
            if (post.getId()>min) min=post.getId();
            else fail();
        }
    }
    @Test
    void getPostsSortedDESC () throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/postsSorted?page=1&sort=DESC"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        Post[] posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post[].class);
        long max=Long.MAX_VALUE;
        for (Post post:posts){
            if (post.getId()<max) max=post.getId();
            else fail();
        }
    }

    @Test
    void deletePost () throws Exception {
        //given
        String title="title test";
        String content="content test";
        LocalDateTime created=LocalDateTime.now();

        Post newPost= new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setCreated(created);
        postRepository.save(newPost);

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/" + newPost.getId()))
                .andReturn();
        boolean empty = postRepository.findById(newPost.getId()).isEmpty();
        if(!empty) fail();
    }
    @Test
    void PostAdd () throws Exception {

        //given
        String title="title test";
        String content="content test";
        LocalDateTime created=LocalDateTime.now();

        Post newPost= new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setCreated(created);
        //when
        mockMvc.perform(MockMvcRequestBuilders
                .post("/posts" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(                        "{" +
                        "\"title\": \""+title+"\"," +
                        "\"content\": \""+content+"\"," +
                        "\"created\": \""+created+"\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        boolean empty = postRepository.findById(newPost.getId()).isEmpty();
        if(!empty) fail();
    }



}