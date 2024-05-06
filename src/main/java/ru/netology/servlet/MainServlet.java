package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_DELETE = "DELETE";

  private static final String API_POSTS_PATH = "/api/posts";
  private static final String REGEX_POST_ID = API_POSTS_PATH + "/\\d+";

  private PostController controller;

  @Override
  public void init() {
    final PostRepository repository = new PostRepository();
    final PostService service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final String path = req.getRequestURI();
      final String method = req.getMethod();

      if (method.equals(METHOD_GET)) {
        if (path.equals(API_POSTS_PATH)) {
          controller.all(resp);
          return;
        } else if (path.matches(REGEX_POST_ID)) {
          controller.getById(req, resp);
          return;
        }
      } else if (method.equals(METHOD_POST) && path.equals(API_POSTS_PATH)) {
        controller.save(req, resp);
        return;
      } else if (method.equals(METHOD_DELETE) && path.matches(REGEX_POST_ID)) {
        controller.removeById(req, resp);
        return;
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

