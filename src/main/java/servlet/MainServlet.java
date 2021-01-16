package servlet;

import controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repository.PostRepository;
import service.PostService;
import springConfig.SpringConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private final static String ENDPOINT = "/api/posts/";

    @Override
    public void init() {
        AnnotationConfigApplicationContext context = new
                AnnotationConfigApplicationContext(SpringConfig.class);
        PostRepository repository = context.getBean("postRepository", PostRepository.class);
        PostService service = context.getBean("postService", PostService.class);
        controller = context.getBean("postController", PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals("GET")) {
                doGet(req, resp);
                return;
            }

            if (method.equals("POST")) {
                doPost(req, resp);
                return;
            }
            if (method.equals("DELETE")) {
                doDelete(req, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var path = req.getRequestURI();
        final var method = req.getMethod();

        if (path.equals(ENDPOINT)) {
            controller.all(resp);
            return;
        }
        if (path.matches(ENDPOINT + "\\d+")) {
            // easy way
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.getById(id, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var path = req.getRequestURI();
        if (path.equals(ENDPOINT)) {
            try {
                controller.save(req.getReader(), resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        final var path = req.getRequestURI();
        if (path.matches(ENDPOINT + "\\d+")) {
            // easy way
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            controller.removeById(id, resp);
        }
    }
}

