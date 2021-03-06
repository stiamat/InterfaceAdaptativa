package com.ufes.interfaceadaptativa.web.rest;

import com.ufes.interfaceadaptativa.domain.Post;
import com.ufes.interfaceadaptativa.repository.PostRepository;
import com.ufes.interfaceadaptativa.service.PostQueryService;
import com.ufes.interfaceadaptativa.service.PostService;
import com.ufes.interfaceadaptativa.service.dto.PostCriteria;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;
import com.ufes.interfaceadaptativa.service.dto.UserDTO;
import com.ufes.interfaceadaptativa.service.mapper.PostMapper;
import com.ufes.interfaceadaptativa.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.ufes.interfaceadaptativa.domain.Post}.
 */
@RestController
@RequestMapping("/api")
public class PostResource {
  private final Logger log = LoggerFactory.getLogger(PostResource.class);

  private static final String ENTITY_NAME = "post";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PostService postService;

  private final PostQueryService postQueryService;

  private final PostRepository postRepository;

  private final PostMapper postMapper;

  public PostResource(
    PostService postService,
    PostQueryService postQueryService,
    PostRepository postRepository,
    PostMapper postMapper
  ) {
    this.postService = postService;
    this.postQueryService = postQueryService;
    this.postRepository = postRepository;
    this.postMapper = postMapper;
  }

  /**
   * {@code POST  /posts} : Create a new post.
   *
   * @param postDTO the postDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postDTO, or with status {@code 400 (Bad Request)} if the post has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/posts")
  public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO)
    throws URISyntaxException {
    log.debug("REST request to save Post : {}", postDTO);
    if (postDTO.getId() != null) {
      throw new BadRequestAlertException(
        "A new post cannot already have an ID",
        ENTITY_NAME,
        "idexists"
      );
    }
    PostDTO result = postService.save(postDTO);
    return ResponseEntity
      .created(new URI("/api/posts/" + result.getId()))
      .headers(
        HeaderUtil.createEntityCreationAlert(
          applicationName,
          false,
          ENTITY_NAME,
          result.getId().toString()
        )
      )
      .body(result);
  }

  /**
   * {@code PUT  /posts} : Updates an existing post.
   *
   * @param postDTO the postDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postDTO,
   * or with status {@code 400 (Bad Request)} if the postDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the postDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/posts")
  public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO)
    throws URISyntaxException {
    log.debug("REST request to update Post : {}", postDTO);
    if (postDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    PostDTO result = postService.save(postDTO);
    return ResponseEntity
      .ok()
      .headers(
        HeaderUtil.createEntityUpdateAlert(
          applicationName,
          false,
          ENTITY_NAME,
          postDTO.getId().toString()
        )
      )
      .body(result);
  }

  /**
   * {@code GET  /posts} : get all the posts.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
   */
  @GetMapping("/posts")
  public ResponseEntity<List<PostDTO>> getAllPosts(
    PostCriteria criteria,
    Pageable pageable
  ) {
    log.debug("REST request to get Posts by criteria: {}", criteria);
    Page<PostDTO> page = postQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
      ServletUriComponentsBuilder.fromCurrentRequest(),
      page
    );
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /posts/count} : count all the posts.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
   */
  @GetMapping("/posts/count")
  public ResponseEntity<Long> countPosts(PostCriteria criteria) {
    log.debug("REST request to count Posts by criteria: {}", criteria);
    return ResponseEntity.ok().body(postQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /posts/:id} : get the "id" post.
   *
   * @param id the id of the postDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/posts/{id}")
  public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
    log.debug("REST request to get Post : {}", id);
    Optional<PostDTO> postDTO = postService.findOne(id);
    return ResponseUtil.wrapOrNotFound(postDTO);
  }

  /**
   * {@code DELETE  /posts/:id} : delete the "id" post.
   *
   * @param id the id of the postDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/posts/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    log.debug("REST request to delete Post : {}", id);
    postService.delete(id);
    return ResponseEntity
      .noContent()
      .headers(
        HeaderUtil.createEntityDeletionAlert(
          applicationName,
          false,
          ENTITY_NAME,
          id.toString()
        )
      )
      .build();
  }

  @RequestMapping(
    value = "/posts/curti/{postId}/{userId}",
    method = RequestMethod.GET
  )
  public ResponseEntity<List<UserDTO>> curti(
    @PathVariable Long postId,
    @PathVariable Long userId
  ) {
    return ResponseEntity.ok().body(postService.curti(postId, userId));
  }

  @RequestMapping(value = "/posts/owl/{userId}", method = RequestMethod.GET)
  public ResponseEntity curti(@PathVariable Long userId) {
    postService.owl(userId);
    return ResponseEntity.ok().body("Processando");
  }

  @RequestMapping(value = "/posts/answer/{postId}", method = RequestMethod.GET)
  public ResponseEntity<List<PostDTO>> respostas(@PathVariable Long postId) {
    Optional<List<Post>> repostas = postRepository.findAnswersPost(postId);
    List<PostDTO> lista;
    if (repostas.isPresent()) {
      lista = postMapper.toDto(repostas.get());
    } else {
      lista = new ArrayList<>();
    }

    return ResponseEntity.ok().body(lista);
  }

  @RequestMapping(
    value = "/posts/answer/user/{userId}",
    method = RequestMethod.GET
  )
  public ResponseEntity<List<PostDTO>> respostasUser(
    @PathVariable Long userId
  ) {
    Optional<List<Post>> repostas = postRepository.findAnswersUser(userId);
    List<PostDTO> lista;
    if (repostas.isPresent()) {
      lista = postMapper.toDto(repostas.get());
    } else {
      lista = new ArrayList<>();
    }

    return ResponseEntity.ok().body(lista);
  }

  @RequestMapping(value = "/posts/search/{login}", method = RequestMethod.GET)
  public ResponseEntity<List<PostDTO>> searchLogin(@PathVariable String login) {
    Optional<List<Post>> repostas = postRepository.searchLogin(login);
    List<PostDTO> lista;
    if (repostas.isPresent()) {
      lista = postMapper.toDto(repostas.get());
    } else {
      lista = new ArrayList<>();
    }

    return ResponseEntity.ok().body(lista);
  }

  @RequestMapping(value = "/posts/search/destaques", method = RequestMethod.GET)
  public ResponseEntity<List<Object>> searchDestaques() {
    Optional<List<Object>> destaques = postRepository.findDestaques();
    if (destaques.isPresent()) {
      return ResponseEntity.ok().body(postRepository.findDestaques().get());
    } else {
      return ResponseEntity.ok().body(new ArrayList<>());
    }
  }
}
