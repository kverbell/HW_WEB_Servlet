package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class PostRepository {
  private final List<Post> posts = new ArrayList<>();
  private long nextId = 1; // Инициализируем счетчик id
  private final ReentrantLock lock = new ReentrantLock();

  public List<Post> all() {
    lock.lock();
    try {
      return new ArrayList<>(posts);
    } finally {
      lock.unlock();
    }
  }

  public Optional<Post> getById(long id) {
    lock.lock();
    try {
      return posts.stream()
              .filter(post -> post.getId() == id)
              .findFirst();
    } finally {
      lock.unlock();
    }
  }

  public Post save(Post post) {
    lock.lock();
    try {
      if (post.getId() == 0) {
        post.setId(nextId++);
        posts.add(post);
      } else {
        for (int i = 0; i < posts.size(); i++) {
          if (posts.get(i).getId() == post.getId()) {
            posts.set(i, post);
            return post;
          }
        }
        throw new IllegalArgumentException("Post with id " + post.getId() + " not found");
      }
      return post;
    } finally {
      lock.unlock();
    }
  }

  public boolean removeById(long id) {
    lock.lock();
    try {
      return posts.removeIf(post -> post.getId() == id);
    } finally {
      lock.unlock();
    }
  }
}
