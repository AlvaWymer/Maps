package com.express.model;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class usertest {

private static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void beforeClass() {
		new SchemaExport(new Configuration().configure("hibernate.cfg.xml")).create(false,true);
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		StandardServiceRegistryBuilder standardServiceRegistryBuilder =
		new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		StandardServiceRegistry standardServiceRegistry = standardServiceRegistryBuilder.build();
		sessionFactory = configuration.buildSessionFactory(standardServiceRegistry);
	}
	@AfterClass
	public static void afterClass() {
		sessionFactory.close();
	}
	
	@Test
	public void testSaveUser() {
		User user=new User();
		user.setUserName("admin");
		user.setPassword("123");
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		//s.save(g);
		s.save(user);
		s.getTransaction().commit();
		s.close();
	}
//	@Test
//	public void testSaveUsers() {
//		testSaveUser();
//		Session s = sessionFactory.getCurrentSession();
//		s.beginTransaction();
//		Category g = (Category)s.get(Category.class, 1);
//		s.getTransaction().commit();
//		System.out.println(g.getId()+""+g.getDescription());
//	}
//	@Test
//	public void test1() {
//		testSaveUser();
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
////		Query q = session.createQuery("from t_blogs b where b.category.id = 1");
////		Query q = session.createQuery("from t_blogs");
////		List<Blog> blogs = (List<Blog>)q.list();
//		Blog u = (Blog)session.get(Blog.class, 1);
//		System.out.println("s");
//		session.getTransaction().commit();
//		session.close();
////		for(Blog c : blogs) {
////			System.out.println(c.getTitle());
////		}
//		System.out.println(u.getTitle());
//		
//	}
//	@Test
//	public void test2() {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		Query q = session.createQuery("from t_admin_user");
//		List<User> users = (List<User>)q.list();
//
//		session.getTransaction().commit();
//		for(User u : users) {
//			System.out.println(u.getUserName());
//		}
//		session.close();
//		
//	}

	
	
//	@Test
//	public void testSchemaExport() {
//		new SchemaExport(new AnnotationConfiguration().configure()).create(false, true);
//		new SchemaExport(new Configuration().configure("hibernate.cfg.xml")).create(false,true);
//	}
	
	public static void main(String[] args) {
		beforeClass();
	}

}
