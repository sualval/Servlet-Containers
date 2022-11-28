package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private AtomicLong counter = new AtomicLong(0);
    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {

        if (post.getId() == 0) {
            post.setId(counter.incrementAndGet());
            posts.put(counter.get(), post);
            return posts.get(counter.get());
        } else if (posts.containsKey(post.getId())) {
            posts.replace(post.getId(), post);
            return posts.get(post.getId());
        } else {
            throw new NotFoundException("id is not available for saving");
        }

    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
        } else {
            throw new NotFoundException("id is not available for remove");
        }
    }
}
