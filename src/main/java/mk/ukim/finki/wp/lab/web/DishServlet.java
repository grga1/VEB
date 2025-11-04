package mk.ukim.finki.wp.lab.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/dish")
public class DishServlet extends HttpServlet {
    private ChefService chefService;
    private DishService dishService;

    @Override
    public void init() {
        WebApplicationContext ctx =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.chefService = ctx.getBean(ChefService.class);
        this.dishService = ctx.getBean(DishService.class);
    }

    // Ако некој отвори /dish со GET, врати го на листата
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/listChefs");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String chefIdStr = req.getParameter("chefId");
        if (chefIdStr == null || chefIdStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/listChefs");
            return;
        }

        Long chefId;
        try { chefId = Long.parseLong(chefIdStr); }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/listChefs");
            return;
        }

        Chef chef = chefService.findById(chefId);
        if (chef == null) {
            resp.sendRedirect(req.getContextPath() + "/listChefs");
            return;
        }

        StringBuilder radios = new StringBuilder();
        for (Dish d : dishService.listDishes()) {
            radios.append("<label><input type='radio' name='dishId' value='")
                    .append(d.getDishId()).append("'> ")
                    .append(d.getName()).append(" (").append(d.getCuisine()).append(")")
                    .append("</label><br/>");
        }

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <title>Add Dish</title>
              <style>
                body{width:800px;margin:auto;font-family:Arial}
                table{border-collapse:collapse;width:100%%}
                td,th{border:1px solid #000;padding:8px}
              </style>
            </head>
            <body>
              <header><h1>Select the Dish to add to the Chef</h1></header>
              <section style="float:left;width:63%%">
                <h2>Select dish:</h2>
                <form action="/chefDetails" method="POST">
                  %s
                  <input type="hidden" name="chefId" value="%d"/>
                  <br/><input type="submit" value="Add dish">
                </form>
              </section>
              <aside style="float:right;width:30%%">
                <table>
                  <tr><td><b>Chef ID</b></td><td>%d</td></tr>
                  <tr><td><b>Chef Name</b></td><td>%s %s</td></tr>
                </table>
              </aside>
            </body>
            </html>
        """.formatted(radios, chefId, chefId, chef.getFirstName(), chef.getLastName());

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(html);
    }
}
