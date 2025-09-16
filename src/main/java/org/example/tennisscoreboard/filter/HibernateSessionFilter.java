package org.example.tennisscoreboard.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import org.example.tennisscoreboard.util.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebFilter("/*")
public class HibernateSessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException {
        Session session = SessionManager.getCurrentSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            chain.doFilter(request, response);
            transaction.commit();
        } catch (Exception e) {
            performRollback(transaction);
            throw new ServletException("Failed transaction for request", e);
        } finally {
            SessionManager.closeCurrentSession();
        }
    }

    private void performRollback(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception rollbackEx) {
                System.err.println("Could not rollback transaction: " + rollbackEx.getMessage());
            }
        }
    }
}