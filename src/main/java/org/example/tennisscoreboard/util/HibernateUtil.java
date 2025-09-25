package org.example.tennisscoreboard.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class HibernateUtil{

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    static {
        try {
            logger.info("Initializing Hibernate SessionFactory...");
            sessionFactory = new Configuration().configure().buildSessionFactory();
            logger.info("Hibernate SessionFactory initialized successfully.");
        } catch (Throwable t) {
            logger.error("Initial SessionFactory creation failed", t);
            throw new ExceptionInInitializerError(t);
        }
    }

    private HibernateUtil() {
        throw new UnsupportedOperationException("HibernateUtil is a utility class and cannot be instantiated");
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

}