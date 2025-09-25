package org.example.tennisscoreboard.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SessionManager{
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    private SessionManager() {
        throw new UnsupportedOperationException("SessionManager is a utility class and cannot be instantiated");
    }

    public static Session getCurrentSession() {
        logger.debug("Getting current session for thread: {}", Thread.currentThread().getId());
        Session currentSession = sessionThreadLocal.get();

        if (currentSession == null) {
            logger.debug("Creating new session for thread: {}", Thread.currentThread().getId());
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            currentSession = sessionFactory.openSession();
            sessionThreadLocal.set(currentSession);
        }
        return currentSession;
    }

    public static void closeCurrentSession() {
        Session currentSession = sessionThreadLocal.get();

        if (currentSession != null && currentSession.isOpen()) {
            logger.debug("Closing session for thread: {}", Thread.currentThread().getId());
            currentSession.close();
        }
        sessionThreadLocal.remove();
    }
}