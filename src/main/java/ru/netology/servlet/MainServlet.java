package ru.netology.servlet;

import ru.netology.congig.AppConfig;
import ru.netology.controller.PostController;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MainServlet extends HttpServlet {
  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_DELETE = "DELETE";

  private static final String API_POSTS_PATH = "/api/posts";
  private static final String REGEX_POST_ID = API_POSTS_PATH + "/\\d+";

  private PostController controller;

  @Override
  public void init() throws ServletException {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      final String path = req.getRequestURI();
      final String method = req.getMethod();

      if (METHOD_GET.equals(method)) {
        if (API_POSTS_PATH.equals(path)) {
          controller.all(resp);
          return;
        } else if (path.matches(REGEX_POST_ID)) {
          controller.getById(req, resp);
          return;
        }
      } else if (METHOD_POST.equals(method) && API_POSTS_PATH.equals(path)) {
        controller.save(req, resp);
        return;
      } else if (METHOD_DELETE.equals(method) && path.matches(REGEX_POST_ID)) {
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

