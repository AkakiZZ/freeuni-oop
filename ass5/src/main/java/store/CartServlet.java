package store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("/cartPage.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        HttpSession session = httpServletRequest.getSession();
        Cart cart = (Cart) session.getAttribute(Cart.NAME);
        ProductDAO dao = (ProductDAO) getServletContext().getAttribute(ProductDAO.NAME);

        String id = httpServletRequest.getParameter("productID");

        if (id != null) {
            try {
                cart.addProduct(dao.getProduct(id), cart.getQuantity(dao.getProduct(id)) + 1);
            } catch (SQLException ignored) { }

        } else {
            Enumeration<String> params = httpServletRequest.getParameterNames();
            while(params.hasMoreElements()){
                id = params.nextElement();
                try {
                    int quantity = Integer.parseInt(httpServletRequest.getParameter(id));
                    Product p = dao.getProduct(id);
                    if (quantity == 0) cart.removeProduct(p);
                    else cart.addProduct(p, quantity);
                } catch (SQLException ignored) { }
            }
        }
        session.setAttribute(Cart.NAME, cart);
        httpServletRequest.getRequestDispatcher("cartPage.jsp").forward(httpServletRequest, httpServletResponse);
    }
}
