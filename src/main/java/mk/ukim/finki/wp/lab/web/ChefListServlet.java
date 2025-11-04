package mk.ukim.finki.wp.lab.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.service.ChefService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/listChefs")
public class ChefListServlet extends HttpServlet {
    private ChefService chefService;

    @Override public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.chefService = ctx.getBean(ChefService.class);
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Chef> chefs = chefService.listChefs();
        StringBuilder radios = new StringBuilder();
        for (Chef c : chefs) {
            radios.append("<label><input type='radio' name='chefId' value='")
                    .append(c.getId()).append("'> Name: ")
                    .append(c.getFirstName()).append(" ").append(c.getLastName())
                    .append(", Bio: ").append(c.getBio())
                    .append("</label><br/>");
        }
        String html = """
            <!DOCTYPE html><html><head><meta charset="utf-8"><title>Chefs</title>
            <style>body{width:800px;margin:auto;font-family:Arial}</style></head><body>
            <header><h1>Welcome to Our Restaurant</h1></header><main>
            <h2>Choose a chef:</h2>
            <form action="/dish" method="POST">%s<input type="submit" value="Submit"></form>
            </main></body></html>
        """.formatted(radios);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(html);
    }
}
