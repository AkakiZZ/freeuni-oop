package login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        httpServletRequest.getRequestDispatcher("login.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException{
        AccountManager manager = (AccountManager) getServletContext().getAttribute(AccountManager.NAME);
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("pass");
        if (manager.accountExists(username, password)) {
            httpServletRequest.getRequestDispatcher("welcome.jsp").forward(httpServletRequest, httpServletResponse);
        } else {
            manager.createAccount(username, password);
            httpServletRequest.getRequestDispatcher("tryAgain.html").forward(httpServletRequest, httpServletResponse);
        }
    }
}
