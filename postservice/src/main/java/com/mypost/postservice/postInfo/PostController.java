
package com.mypost.postservice.postInfo;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mypost.postservice.Userr;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController

@RequestMapping("/users/{userid}")
public class PostController {

    private final PostRepository postRepository;
    private final PostModelAssembler assembler;
  
    private final HashtagRepository hashtagRepository;
    private final UserClient userClient; // Add the UserClient

  
    private Cloudinary cloudinary;

    @Autowired
    public PostController(PostRepository postRepository, PostModelAssembler assembler,
                          HashtagRepository hashtagRepository, UserClient userClient) {
        this.postRepository = postRepository;
        this.assembler = assembler;
      
        this.hashtagRepository = hashtagRepository;
        this.userClient = userClient; // Initialize UserClient
    }

    @GetMapping("/posts/{postid}")
    @Transactional
    public EntityModel<Post> one(@PathVariable("userid") Integer userid, @PathVariable("postid") Integer postid) {
        Userr user  = userClient.getUser(userid);

        if (user == null) {
            throw new RuntimeException("User not found with id: " + userid);
            }

        Post post = postRepository.findById(postid)
                .orElseThrow(() -> new PostNotFoundException(postid));
        return assembler.toModel(post);
    }

    @GetMapping("/posts")
    public CollectionModel<EntityModel<Post>> all(@PathVariable("userid") Integer userId) {
        List<EntityModel<Post>> posts = postRepository.findByUserId(userId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all(userId)).withSelfRel());
    }

    // @PostMapping("/posts")
    // public ResponseEntity<?> addPost(@PathVariable Integer userid, @Valid @RequestBody Post post) {
    //     // Fetch user details using the Feign client
    //     User userDTO = userClient.getUser(userid);

    //     // Set user details
    //     User user = new User();
    //     user.setId(userDTO.getId());
    //     user.setUsername(userDTO.getUsername());
    //     user.setEmail(userDTO.getEmail());
    //     // Set other fields as needed

    //     post.setUser(user);
    //     post.setDate(LocalDateTime.now());

    //     // Extract hashtags
    //     Set<String> hashtagsInContent = extractHashtagsFromContent(post.getCaption());
    //     for (String hashtagName : hashtagsInContent) {
    //         Optional<Hashtag> existingHashtagOptional = hashtagRepository.findByName(hashtagName);
    //         Hashtag hashtag;
    //         if (existingHashtagOptional.isPresent()) {
    //             hashtag = existingHashtagOptional.get();
    //         } else {
    //             hashtag = new Hashtag();
    //             hashtag.setName(hashtagName);
    //             hashtagRepository.save(hashtag);
    //         }
    //         hashtag.getPosts().add(post);
    //         post.getHashtags().add(hashtag);
    //     }

    //     Post savedPost = postRepository.save(post);
    //     EntityModel<Post> entityModel = assembler.toModel(savedPost);

    //     return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
    //             .body(entityModel);
    // }

    @PostMapping("/posts")
public ResponseEntity<?> addPosts(@PathVariable Integer userid,
                                  @RequestParam(value="image" , required = false) MultipartFile image,
                                  @RequestParam(value = "video", required = false) MultipartFile video,
                                  @RequestParam("caption") String caption,@RequestParam("Audiance") Privacy audiance) {
                                    Userr user  = userClient.getUser(userid);

                                    if (user == null) {
                                        throw new RuntimeException("User not found with id: " + userid);
                                        }

    try {
        String imageUrl = null;
        String videoUrl = null;

        // Upload image to Cloudinary if provided
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadToCloudinary(image, "image");
        }

        // Upload video to Cloudinary if provided
        if (video != null && !video.isEmpty()) {
            videoUrl = uploadToCloudinary(video, "video");
        }



        
        // Save post with image and/or video URLs
        Post newPost = new Post(caption,audiance, imageUrl, videoUrl);
        newPost.setUserId(user.getId());
        Post savedPost = postRepository.save(newPost);
        EntityModel<Post> entityModel = assembler.toModel(savedPost);

     /////////////for hashtag//////////////////////
        String content=  newPost.getCaption();
        Set<String> hashtagsInContent = extractHashtagsFromContent(content);
  
  
  for (String hashtagName : hashtagsInContent) {
      Optional<Hashtag> existingHashtagOptional = hashtagRepository.findByName(hashtagName);
      if (existingHashtagOptional.isPresent()) {
          Hashtag existingHashtag = existingHashtagOptional.get();
          existingHashtag.getPosts().add(newPost);
          hashtagRepository.save(existingHashtag);
          newPost.getHashtags().add(existingHashtag);
          postRepository.save(newPost);
          System.out.println(existingHashtag.getName());
      } else {
          Hashtag newHashtag = new Hashtag();
          newHashtag.setName(hashtagName);
          newHashtag.getPosts().add(newPost);
          hashtagRepository.save(newHashtag);
          newPost.getHashtags().add(newHashtag);
          postRepository.save(newPost);
          System.out.println(newHashtag.getName());
          
      }
  }
      ///////////////////////////////////////
        
          return ResponseEntity
                  .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                  .body(entityModel);
      

    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading media");
    }
}

private String uploadToCloudinary(MultipartFile file, String resourceType) throws IOException {
    try {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(), 
                ObjectUtils.asMap("resource_type", resourceType));
        return (String) uploadResult.get("secure_url");
    } catch (Exception e) {
        // Handle Cloudinary specific upload errors here (e.getMessage())
        throw new RuntimeException("Error uploading file to Cloudinary: " + e.getMessage());
    }
}




@PutMapping("/posts/{postId}")
public ResponseEntity<?> editPost(
    @PathVariable Integer postId,
    @RequestParam(value = "image", required = false) MultipartFile image,
    @RequestParam(value = "video", required = false) MultipartFile video,
    @RequestParam("caption") String caption,
    @RequestParam("Audiance") Privacy audiance,
    @RequestParam(value = "removeImage", required = false) boolean removeImage,
    @RequestParam(value = "removeVideo", required = false) boolean removeVideo
) {
    try {
        // Find post by postId
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        // Update post fields from the request body
        post.setCaption(caption);
        post.setDate(LocalDateTime.now());
        post.setAudiance(audiance);

        // Remove image if requested
        if (removeImage) {
            post.setImageUrl(null); // Set image URL to null or remove it from the database
        }

        // Remove video if requested
        if (removeVideo) {
            post.setVideoUrl(null); // Set video URL to null or remove it from the database
        }

        // Upload image to Cloudinary if provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadToCloudinary(image, "image");
            post.setImageUrl(imageUrl);
        }

        // Upload video to Cloudinary if provided
        if (video != null && !video.isEmpty()) {
            String videoUrl = uploadToCloudinary(video, "video");
            post.setVideoUrl(videoUrl);
        }

        // Save the updated post
        Post savedPost = postRepository.save(post);
        EntityModel<Post> entityModel = assembler.toModel(savedPost);

        return ResponseEntity.ok(entityModel);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating post");
    }
}

    private Set<String> extractHashtagsFromContent(String content) {
        Set<String> hashtags = new HashSet<>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("userid") Integer userid, @PathVariable("postId") Integer postid) {
        if (!postRepository.existsById(postid)) {
            throw new PostNotFoundException(postid);
        }

        Post originalPost = postRepository.findById(postid)
                .orElseThrow(() -> new PostNotFoundException(postid));

        List<Post> sharedPosts = originalPost.getSharedPosts();
        for (Post sharedPost : sharedPosts) {
            postRepository.deleteById(sharedPost.getId());
        }

        postRepository.deleteById(postid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shared-posts")
    public ResponseEntity<?> addsharePost(@PathVariable Integer userid, @RequestParam Integer postId) {
        // Fetch user details using the Feign client
        Userr sharingUserDTO = userClient.getUser(userid);

        if (sharingUserDTO == null) {
            throw new RuntimeException("User not found with id: " + userid);
            }
    

        // Set user details
        Userr sharingUser = new Userr();
        sharingUser.setId(sharingUserDTO.getId());
        sharingUser.setUsername(sharingUserDTO.getUsername());
        sharingUser.setEmail(sharingUserDTO.getEmail());
        // Set other fields as needed

        Post originalPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Post sharedPost = new Post(originalPost.getCaption(), originalPost.getAudiance(), originalPost.getImageUrl(), originalPost.getVideoUrl());
        sharedPost.setUserId(sharingUser.getId());
        sharedPost.setOriginalPost(originalPost);
        originalPost.getSharedPosts().add(sharedPost);
        sharedPost.setShared(true);

        Post savedSharedPost = postRepository.save(sharedPost);
        // userRepository.save(sharingUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(savedSharedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/shared-posts")
    public CollectionModel<EntityModel<Post>> getSharedPosts(@PathVariable("userid") Integer userid) {
        Userr user  = userClient.getUser(userid);

        if (user == null) {
            throw new RuntimeException("User not found with id: " + userid);
            }
    

        List<EntityModel<Post>> sharedPosts = user.getPosts().stream()
                .filter(Post::isShared)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(sharedPosts, linkTo(methodOn(PostController.class).getSharedPosts(userid)).withSelfRel());
    }
}





