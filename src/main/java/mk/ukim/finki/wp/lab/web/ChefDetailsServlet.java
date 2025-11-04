package mk.ukim.finki.wp.lab.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/chefDetails")
public class ChefDetailsServlet extends HttpServlet {
    private ChefService chefService;

    @Override public void init() {
        WebApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.chefService = ctx.getBean(ChefService.class);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long chefId = Long.parseLong(req.getParameter("chefId"));
        String dishId = req.getParameter("dishId");

        Chef chef = chefService.addDishToChef(chefId, dishId);

        StringBuilder lis = new StringBuilder();
        for (Dish d : chef.getDishes()) {
            lis.append("<li>").append(d.getName())
                    .append(" (").append(d.getCuisine()).append(", ")
                    .append(d.getPreparationTime()).append(" min)</li>");
        }

        String html = """
            <!DOCTYPE html><html><head><meta charset="utf-8"><title>Chef Details</title>
            <style>body{width:800px;margin:auto;font-family:Arial}ul{list-style:none;padding:0}li{background:#f5f5f5;margin:5px 0;padding:6px;border-radius:3px}</style>
            </head><body>
            <header><h1>Chef: %s %s</h1></header>
            <section><h2>Bio: %s</h2><h2>Dishes prepared by this chef:</h2>
            <ul>%s</ul></section></body></html>
        """.formatted(chef.getFirstName(), chef.getLastName(), chef.getBio(), lis);

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(html);
    }
}
