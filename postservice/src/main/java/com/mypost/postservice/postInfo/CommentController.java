package com.mypost.postservice.postInfo;



import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mypost.postservice.UserClient;
import com.mypost.postservice.Userr;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/posts/{postId}/comments")

public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentModelAssembler assembler;
    private final PostRepository postRepository;
    private final UserClient userClient;
    private final CommentLikeRepository commentLikeRepository;
    
   @Autowired
    public CommentController(CommentRepository commentRepository, CommentModelAssembler assembler,
                             PostRepository postRepository,UserClient userClient,
                             CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.assembler = assembler;
        this.postRepository = postRepository;
        this.userClient = userClient; 
        this.commentLikeRepository = commentLikeRepository;
    }

    @GetMapping("/{commentId}")
    public EntityModel<Comment> getComment(@PathVariable Integer postId,@PathVariable Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        return assembler.toModel(comment);
    }

    @GetMapping
    public CollectionModel<EntityModel<Comment>> getCommentsByPost(@PathVariable Integer postId) {
        List<EntityModel<Comment>> comments = commentRepository.findByPost_Id(postId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(comments);
    }

    @PostMapping("/users/{commentUserId}")
    public ResponseEntity<?> addComment(@PathVariable("postId") Integer postId,@Valid @PathVariable("commentUserId")  Integer commentUserId,
                                        @RequestBody Comment newComment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

                Userr user  = userClient.getUser(commentUserId);

                if (user == null) {
                    throw new RuntimeException("User not found with id: " + commentUserId);
                    }

        newComment.setDate(LocalDateTime.now());
        newComment.setPost(post);
        newComment.setUserId(user.getId());

        Comment savedComment = commentRepository.save(newComment);
        EntityModel<Comment> entityModel = assembler.toModel(savedComment);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable Integer commentId,@Valid @RequestBody Comment updatedComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        comment.setCommentContent(updatedComment.getCommentContent());

        Comment savedComment = commentRepository.save(comment);
        EntityModel<Comment> entityModel = assembler.toModel(savedComment);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException(commentId);
        }

        // Delete related CommentLikes
        commentLikeRepository.deleteByComment_Id(commentId);

        commentRepository.deleteById(commentId);

        return ResponseEntity.noContent().build();
    }

    


}


