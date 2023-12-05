package com.ecom.Controller.admin;

import com.ecom.Entity.Post;
import com.ecom.Entity.User;
import com.ecom.Model.request.CreatePostRequest;
import com.ecom.Service.ImageService;
import com.ecom.Service.PostService;
import com.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@CrossOrigin("*")
public class PostsController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/api/admin/posts")
    public ResponseEntity<Object> getListPosts(@RequestParam(defaultValue = "", required = false) String title,
                                               @RequestParam(defaultValue = "", required = false) String status,
                                               @RequestParam(defaultValue = "1", required = false) Integer page) {
        Page<Post> posts = postService.adminGetListPosts(title, status, page);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> getProductDetail(@PathVariable Long id) {
        Post rs = postService.getPostById(id);
        return ResponseEntity.ok(rs);
    }

//    @GetMapping("/api/admin/posts")
//    public ResponseEntity<Object> findByTitle(@RequestParam String title) {
//        Post rs = postService.getPostByTitle(title);
//        return ResponseEntity.ok(rs);
//    }

    @PostMapping("/api/admin/posts")
    public ResponseEntity<Object> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        User user = userService.findByEmail(createPostRequest.getEmail());
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post post = postService.createPost(createPostRequest, user);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody CreatePostRequest createPostRequest, @PathVariable Long id) {
        User user = userService.findByEmail(createPostRequest.getEmail());
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        postService.updatePost(createPostRequest, user, id);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable int id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
