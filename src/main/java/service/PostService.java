package service;

import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.PostRepository;

import java.util.List;


public class PostService {
    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public String getById(long id) {
        return repository.getById(id);
    }

    public Post save(Post post) throws Exception {
        return repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}

