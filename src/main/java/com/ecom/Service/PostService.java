package com.ecom.Service;

import com.ecom.Entity.Post;
import com.ecom.Entity.User;
import com.ecom.Exception.BadRequestException;
import com.ecom.Exception.InternalServerException;
import com.ecom.Exception.NotFoundException;
import com.ecom.Model.dto.PageableDTO;
import com.ecom.Model.dto.PostDTO;
import com.ecom.Model.request.CreatePostRequest;
import com.ecom.Repository.PostRepository;
import com.ecom.utils.PageUtil;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.ecom.config.Contant.*;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public PageableDTO adminGetListPost(String title, String status, int page) {
        PageUtil pageUtil = new PageUtil(LIMIT_POST, page);

        // Get list posts and totalItems
        List<PostDTO> posts = postRepository.adminGetListPost(title, status, LIMIT_POST, pageUtil.calculateOffset());
        int totalItems = postRepository.countPostFilter(title, status);

        int totalPages = pageUtil.calculateTotalPage(totalItems);

        return new PageableDTO(posts, totalItems, totalPages, pageUtil.getPage());
    }


    public Post createPost(CreatePostRequest createPostRequest, User user) {
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        Slugify slg = new Slugify();
        post.setSlug(slg.slugify(createPostRequest.getTitle()));
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setCreatedBy(user);

        if (createPostRequest.getStatus() == PUBLIC_POST) {
            // Public post
            if (createPostRequest.getDescription().isEmpty()) {
                throw new BadRequestException("Để công khai bài viết vui lòng nhập mô tả");
            }
            if (createPostRequest.getImage().isEmpty()) {
                throw new BadRequestException("Vui lòng chọn ảnh cho bài viết trước khi công khai");
            }
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            if (createPostRequest.getStatus() != DRAFT_POST) {
                throw new BadRequestException("Trạng thái bài viết không hợp lệ");
            }
        }
        post.setDescription(createPostRequest.getDescription());
        post.setThumbnail(createPostRequest.getImage());
        post.setStatus(createPostRequest.getStatus());

        postRepository.save(post);

        return post;
    }

    public void updatePost(CreatePostRequest createPostRequest, User user, Long id) {
        Optional<Post> rs = postRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("Bài viết không tồn tại");
        }
        Post post = rs.get();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        Slugify slug = new Slugify();
        post.setSlug(slug.slugify(createPostRequest.getTitle()));
        post.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        post.setModifiedBy(user);
        if (createPostRequest.getStatus() == PUBLIC_POST) {
            // Public post
            if (createPostRequest.getDescription().isEmpty()) {
                throw new BadRequestException("Để công khai bài viết vui lòng nhập mô tả");
            }
            if (createPostRequest.getImage().isEmpty()) {
                throw new BadRequestException("Vui lòng chọn ảnh cho bài viết trước khi công khai");
            }
            if (post.getPublishedAt() == null) {
                post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            }
        } else {
            if (createPostRequest.getStatus() != DRAFT_POST) {
                throw new BadRequestException("Trạng thái bài viết không hợp lệ");
            }
        }
        post.setDescription(createPostRequest.getDescription());
        post.setThumbnail(createPostRequest.getImage());
        post.setStatus(createPostRequest.getStatus());

        try {
            postRepository.save(post);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi cập nhật bài viết");
        }
    }

    public void deletePost(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new NotFoundException("Bài viết không tồn tại");
        }
        try {
            postRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi xóa bài viết");
        }
    }

    public Post getPostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new NotFoundException("Bài viết không tồn tại");
        }
        return post.get();
    }

//    public Post getPostByTitle(String title) {
//        Optional<Post> post = postRepository.findByTitle(title);
//        if (post.isEmpty()) {
//            throw new NotFoundException("Bài viết không tồn tại");
//        }
//        return post.get();
//    }

    public Page<Post> adminGetListPosts(String title, String status, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_POST, Sort.by("published_at").descending());
        return postRepository.adminGetListPosts(title, status, pageable);
    }

    public List<Post> getLatesPost() {
        return postRepository.getLatesPosts(PUBLIC_POST, LIMIT_POST_NEW);
    }

    public Page<Post> getListPost(int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_POST_SHOP, Sort.by("publishedAt").descending());
        return postRepository.findAllByStatus(PUBLIC_POST, pageable);
    }

    public List<Post> getLatestPostsNotId(long id) {
        return postRepository.getLatestPostsNotId(PUBLIC_POST,id,LIMIT_POST_RELATED);
    }


    public long getCountPost() {
        return postRepository.count();
    }
}

