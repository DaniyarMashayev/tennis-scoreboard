package org.example.tennisscoreboard.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil{

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable t) {
            System.err.println("SessionFactory creation failed" + t);
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