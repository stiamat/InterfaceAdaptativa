package com.ufes.interfaceadaptativa.web.rest;

import com.ufes.interfaceadaptativa.InterfaceAdaptativaApp;
import com.ufes.interfaceadaptativa.domain.Post;
import com.ufes.interfaceadaptativa.domain.User;
import com.ufes.interfaceadaptativa.repository.PostRepository;
import com.ufes.interfaceadaptativa.service.PostService;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;
import com.ufes.interfaceadaptativa.service.mapper.PostMapper;
import com.ufes.interfaceadaptativa.service.dto.PostCriteria;
import com.ufes.interfaceadaptativa.service.PostQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.ufes.interfaceadaptativa.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PostResource} REST controller.
 */
@SpringBootTest(classes = InterfaceAdaptativaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PostResourceIT {

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Long DEFAULT_LIKES = 1L;
    private static final Long UPDATED_LIKES = 2L;
    private static final Long SMALLER_LIKES = 1L - 1L;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostMockMvc;

    private Post post;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createEntity(EntityManager em) {
        Post post = new Post()
            .body(DEFAULT_BODY)
            .date(DEFAULT_DATE)
            .active(DEFAULT_ACTIVE)
            .likes(DEFAULT_LIKES);
        return post;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createUpdatedEntity(EntityManager em) {
        Post post = new Post()
            .body(UPDATED_BODY)
            .date(UPDATED_DATE)
            .active(UPDATED_ACTIVE)
            .likes(UPDATED_LIKES);
        return post;
    }

    @BeforeEach
    public void initTest() {
        post = createEntity(em);
    }

    @Test
    @Transactional
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();
        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);
        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testPost.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPost.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPost.getLikes()).isEqualTo(DEFAULT_LIKES);
    }

    @Test
    @Transactional
    public void createPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post with an existing ID
        post.setId(1L);
        PostDTO postDTO = postMapper.toDto(post);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES.intValue())));
    }
    
    @Test
    @Transactional
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES.intValue()));
    }


    @Test
    @Transactional
    public void getPostsByIdFiltering() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        Long id = post.getId();

        defaultPostShouldBeFound("id.equals=" + id);
        defaultPostShouldNotBeFound("id.notEquals=" + id);

        defaultPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPostShouldNotBeFound("id.greaterThan=" + id);

        defaultPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPostShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPostsByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where body equals to DEFAULT_BODY
        defaultPostShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the postList where body equals to UPDATED_BODY
        defaultPostShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllPostsByBodyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where body not equals to DEFAULT_BODY
        defaultPostShouldNotBeFound("body.notEquals=" + DEFAULT_BODY);

        // Get all the postList where body not equals to UPDATED_BODY
        defaultPostShouldBeFound("body.notEquals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllPostsByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where body in DEFAULT_BODY or UPDATED_BODY
        defaultPostShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the postList where body equals to UPDATED_BODY
        defaultPostShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllPostsByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where body is not null
        defaultPostShouldBeFound("body.specified=true");

        // Get all the postList where body is null
        defaultPostShouldNotBeFound("body.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByBodyContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where body contains DEFAULT_BODY
        defaultPostShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the postList where body contains UPDATED_BODY
        defaultPostShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllPostsByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where body does not contain DEFAULT_BODY
        defaultPostShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the postList where body does not contain UPDATED_BODY
        defaultPostShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }


    @Test
    @Transactional
    public void getAllPostsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date equals to DEFAULT_DATE
        defaultPostShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the postList where date equals to UPDATED_DATE
        defaultPostShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date not equals to DEFAULT_DATE
        defaultPostShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the postList where date not equals to UPDATED_DATE
        defaultPostShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date in DEFAULT_DATE or UPDATED_DATE
        defaultPostShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the postList where date equals to UPDATED_DATE
        defaultPostShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date is not null
        defaultPostShouldBeFound("date.specified=true");

        // Get all the postList where date is null
        defaultPostShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date is greater than or equal to DEFAULT_DATE
        defaultPostShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the postList where date is greater than or equal to UPDATED_DATE
        defaultPostShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date is less than or equal to DEFAULT_DATE
        defaultPostShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the postList where date is less than or equal to SMALLER_DATE
        defaultPostShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date is less than DEFAULT_DATE
        defaultPostShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the postList where date is less than UPDATED_DATE
        defaultPostShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where date is greater than DEFAULT_DATE
        defaultPostShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the postList where date is greater than SMALLER_DATE
        defaultPostShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllPostsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where active equals to DEFAULT_ACTIVE
        defaultPostShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the postList where active equals to UPDATED_ACTIVE
        defaultPostShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPostsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where active not equals to DEFAULT_ACTIVE
        defaultPostShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the postList where active not equals to UPDATED_ACTIVE
        defaultPostShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPostsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultPostShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the postList where active equals to UPDATED_ACTIVE
        defaultPostShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPostsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where active is not null
        defaultPostShouldBeFound("active.specified=true");

        // Get all the postList where active is null
        defaultPostShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes equals to DEFAULT_LIKES
        defaultPostShouldBeFound("likes.equals=" + DEFAULT_LIKES);

        // Get all the postList where likes equals to UPDATED_LIKES
        defaultPostShouldNotBeFound("likes.equals=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes not equals to DEFAULT_LIKES
        defaultPostShouldNotBeFound("likes.notEquals=" + DEFAULT_LIKES);

        // Get all the postList where likes not equals to UPDATED_LIKES
        defaultPostShouldBeFound("likes.notEquals=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes in DEFAULT_LIKES or UPDATED_LIKES
        defaultPostShouldBeFound("likes.in=" + DEFAULT_LIKES + "," + UPDATED_LIKES);

        // Get all the postList where likes equals to UPDATED_LIKES
        defaultPostShouldNotBeFound("likes.in=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes is not null
        defaultPostShouldBeFound("likes.specified=true");

        // Get all the postList where likes is null
        defaultPostShouldNotBeFound("likes.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes is greater than or equal to DEFAULT_LIKES
        defaultPostShouldBeFound("likes.greaterThanOrEqual=" + DEFAULT_LIKES);

        // Get all the postList where likes is greater than or equal to UPDATED_LIKES
        defaultPostShouldNotBeFound("likes.greaterThanOrEqual=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes is less than or equal to DEFAULT_LIKES
        defaultPostShouldBeFound("likes.lessThanOrEqual=" + DEFAULT_LIKES);

        // Get all the postList where likes is less than or equal to SMALLER_LIKES
        defaultPostShouldNotBeFound("likes.lessThanOrEqual=" + SMALLER_LIKES);
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes is less than DEFAULT_LIKES
        defaultPostShouldNotBeFound("likes.lessThan=" + DEFAULT_LIKES);

        // Get all the postList where likes is less than UPDATED_LIKES
        defaultPostShouldBeFound("likes.lessThan=" + UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void getAllPostsByLikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where likes is greater than DEFAULT_LIKES
        defaultPostShouldNotBeFound("likes.greaterThan=" + DEFAULT_LIKES);

        // Get all the postList where likes is greater than SMALLER_LIKES
        defaultPostShouldBeFound("likes.greaterThan=" + SMALLER_LIKES);
    }


    @Test
    @Transactional
    public void getAllPostsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        post.setUser(user);
        postRepository.saveAndFlush(post);
        Long userId = user.getId();

        // Get all the postList where user equals to userId
        defaultPostShouldBeFound("userId.equals=" + userId);

        // Get all the postList where user equals to userId + 1
        defaultPostShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostShouldBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES.intValue())));

        // Check, that the count call also returns 1
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostShouldNotBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = postRepository.findById(post.getId()).get();
        // Disconnect from session so that the updates on updatedPost are not directly saved in db
        em.detach(updatedPost);
        updatedPost
            .body(UPDATED_BODY)
            .date(UPDATED_DATE)
            .active(UPDATED_ACTIVE)
            .likes(UPDATED_LIKES);
        PostDTO postDTO = postMapper.toDto(updatedPost);

        restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testPost.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPost.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPost.getLikes()).isEqualTo(UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void updateNonExistingPost() throws Exception {
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Delete the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
