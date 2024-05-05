package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    List<Post> data = service.all();
    writeResponse(response, data);
  }

  public void getById(HttpServletRequest req, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    long id = extractPostIdFromURL(req.getRequestURI());
    Post post = service.getById(id);
    if (post != null) {
      writeResponse(response, post);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  public void save(HttpServletRequest req, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    Gson gson = new Gson();
    try (Reader reader = req.getReader()) {
      Post post = gson.fromJson(reader, Post.class);
      Post savedPost = service.save(post);
      writeResponse(response, savedPost);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().print(gson.toJson("Invalid request body"));
    }
  }

  public void removeById(HttpServletRequest req, HttpServletResponse response) {
    response.setContentType(APPLICATION_JSON);
    long id = extractPostIdFromURL(req.getRequestURI());
    service.removeById(id);
    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  private void writeResponse(HttpServletResponse response, Object data) throws IOException {
    Gson gson = new Gson();
    response.getWriter().print(gson.toJson(data));
  }

  private long extractPostIdFromURL(String url) {
    return Long.parseLong(url.substring(url.lastIndexOf("/") + 1));
  }
}
