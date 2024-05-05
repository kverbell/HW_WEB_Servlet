package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
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

      if (method.equals("GET")) {
        if (path.equals("/api/posts")) {
          controller.all(resp);
          return;
        } else if (path.matches("/api/posts/\\d+")) {
          controller.getById(req, resp);
          return;
        }
      } else if (method.equals("POST") && path.equals("/api/posts")) {
        controller.save(req, resp);
        return;
      } else if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
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

