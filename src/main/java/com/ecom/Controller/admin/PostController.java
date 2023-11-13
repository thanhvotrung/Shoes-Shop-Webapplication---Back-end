package com.ecom.Controller.admin;

import com.ecom.Entity.Post;
import com.ecom.Entity.User;
import com.ecom.Model.request.CreatePostRequest;
import com.ecom.Security.CustomUserDetails;
import com.ecom.Service.ImageService;
import com.ecom.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/api/admin/posts")
    public ResponseEntity<Object> getListPosts(@RequestParam(defaultValue = "", required = false) String title,
                                               @RequestParam(defaultValue = "", required = false) String status,
                                               @RequestParam(defaultValue = "1", required = false) Integer page) {
        Page<Post> posts = postService.adminGetListPosts(title, status, page);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/api/admin/posts")
    public ResponseEntity<Object> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Post post = postService.createPost(createPostRequest, user);

        return ResponseEntity.ok(post);
    }

    @PutMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> updatePost(@Valid @RequestBody CreatePostRequest createPostRequest, @PathVariable long id) {
        User user = ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUser();
        postService.updatePost(createPostRequest, user, id);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
