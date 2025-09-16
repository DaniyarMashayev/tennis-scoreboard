package org.example.tennisscoreboard.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public final class SessionManager{
    private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    private SessionManager() {
        throw new UnsupportedOperationException("SessionManager is a utility class and cannot be instantiated");
    }

    public static Session getCurrentSession() {
        Session currentSession = sessionThreadLocal.get();

        if (currentSession == null) {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            currentSession = sessionFactory.openSession();
            sessionThreadLocal.set(currentSession);
        }
        return currentSession;
    }

    public static void closeCurrentSession() {
        Session currentSession = sessionThreadLocal.get();

        if (currentSession != null && currentSession.isOpen()) {
            currentSession.close();
        }
        sessionThreadLocal.remove();
    }
}