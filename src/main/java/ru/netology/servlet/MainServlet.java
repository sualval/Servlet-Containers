package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private final String GET = "GET";
    private final String POST = "POST";
    private final String DELETE = "DELETE";
    private final String PATH = "/api/posts";
    private final String PATH_ID = "/api/posts/\\d+";
    private PostController controller;

    @Override
    public void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            if (path.equals(PATH)) {
                if (method.equals(GET)) {
                    controller.all(resp);
                    return;
                }
                if (method.equals(POST)) {
                    controller.save(req.getReader(), resp);
                }
                return;
            }
            if (path.matches(PATH_ID)) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                if (method.equals(GET)) {
                    controller.getById(id, resp);
                    return;
                }
                if (method.equals(DELETE)) {
                    controller.removeById(id, resp);
                }
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}

