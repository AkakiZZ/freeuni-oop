package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/create")
public class CreateAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("newAccount.html").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        AccountManager manager = (AccountManager) getServletContext().getAttribute(AccountManager.NAME);
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("pass");
        if (manager.usernameOccupied(username)) {
            httpServletRequest.getRequestDispatcher("alreadyUsed.jsp").forward(httpServletRequest, httpServletResponse);
        } else {
            manager.createAccount(username, password);
            httpServletRequest.getRequestDispatcher("welcome.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }
}
