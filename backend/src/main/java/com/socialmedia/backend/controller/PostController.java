package com.socialmedia.backend.controller;

import com.socialmedia.backend.model.Comment;
import com.socialmedia.backend.model.CommentRequest;
import com.socialmedia.backend.model.Post;
import com.socialmedia.backend.model.User;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody Post post,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post newPost = new Post(
                user.getUsername(),
                post.getContent()
        );

        newPost.setImageUrl(post.getImageUrl());

        Post savedPost = postRepository.save(newPost);

        return ResponseEntity.ok(savedPost);
    }

    @GetMapping
    public List<Post> getPosts() {

        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(
            @PathVariable String id) {

        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable String id,
            Authentication authentication) {

        return postRepository.findById(id)
                .map(post -> {

                    String email = authentication.getName();

                    User user = userRepository
                            .findByEmail(email)
                            .orElseThrow();

                    if (!post.getAuthorUsername()
                            .equals(user.getUsername())) {

                        return ResponseEntity
                                .status(403)
                                .body("You can only delete your own posts");
                    }

                    postRepository.deleteById(id);

                    return ResponseEntity.ok(
                            "Post deleted successfully"
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(
            @PathVariable String id,
            Authentication authentication) {

        return postRepository.findById(id)
                .map(post -> {

                    String email = authentication.getName();

                    if (post.getLikes().contains(email)) {
                        post.getLikes().remove(email);

                        postRepository.save(post);

                        return ResponseEntity.ok(
                                "Post unliked"
                        );
                    }

                    post.getLikes().add(email);

                    postRepository.save(post);

                    return ResponseEntity.ok(
                            "Post liked"
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable String id,
            @RequestBody CommentRequest request,
            Authentication authentication) {

        if (request.getText() == null ||
                request.getText().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Comment cannot be empty");
        }

        return postRepository.findById(id)
                .map(post -> {

                    String email = authentication.getName();

                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    Comment comment = new Comment(
                            user.getUsername(),
                            request.getText(),
                            LocalDateTime.now()
                    );

                    post.getComments().add(comment);

                    postRepository.save(post);

                    return ResponseEntity.ok(comment);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}