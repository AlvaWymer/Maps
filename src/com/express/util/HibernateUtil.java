package com.express.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

  
public class HibernateUtil {
	private static SessionFactory sessionfactory;

	static {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		StandardServiceRegistryBuilder standardServiceRegistryBuilder =
		new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		StandardServiceRegistry standardServiceRegistry = standardServiceRegistryBuilder.build();
		sessionfactory = configuration.buildSessionFactory(standardServiceRegistry);
	}
	
	public static SessionFactory getSessionFactory() {
	    return sessionfactory;
	}
	
	public static Session getSession() {
	    return sessionfactory.openSession();
	}
	
	public static Session getCuttSession() {
	    return sessionfactory.getCurrentSession();
	}
	
	public static void closeSession(Session session) {
	    if (session != null) {
	        if (session.isOpen()) {
	        	
	            session.close();
	        }
	    }
	}
}
