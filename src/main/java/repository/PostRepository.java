package repository;

import model.Post;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PostRepository {

  ConcurrentHashMap<Long, String> repositoryMap = new ConcurrentHashMap<>();
  volatile long counter = 0;

  public List<Post> all() {
    return Collections.emptyList();
  }

  public String getById(long id) {
    return repositoryMap.get(id);
  }

  public synchronized Post save(Post post) throws Exception {
    if (post.getId() == 0) {
      counter++;
      repositoryMap.put(counter, post.getContent());
    }

    if(post.getId() != 0) {
      if (repositoryMap.containsKey(post.getId())) {
        throw new Exception("There is no such id post in this map");
      }
      repositoryMap.put(post.getId(), post.getContent());
    }
    return post;
  }

  public void removeById(long id) {
  }
}
