package cg.wbd.grandemonstration.service.impl;

import cg.wbd.grandemonstration.model.Customer;
import cg.wbd.grandemonstration.service.CustomerService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Map;
//@Service
public class HibernateCustomerServiceImpl implements CustomerService {
    private static final Map<Integer,Customer> customers = null;
    static {
        try{
            SessionFactory sessionFactory= new Configuration().configure("hibernate.conf.xml").buildSessionFactory();
            sessionFactory.close();
        }catch (HibernateException e){
            e.printStackTrace();
        }
    }
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    static {
        try{
            sessionFactory=new Configuration().configure("hibernate.conf.xml").buildSessionFactory();
            entityManager=sessionFactory.createEntityManager();
        }catch (HibernateException e){
            e.printStackTrace();
        }
    }
    @Override
    public List<Customer> findAll() {
        String queryStr="SELECT c FROM Customer AS c";
        TypedQuery<Customer> query=entityManager.createQuery(queryStr,Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findOne(Long id) {
       String query="SELECT c FROM Customer AS c WHERE c.id=:id ";
       TypedQuery<Customer>query1=entityManager.createQuery(query,Customer.class);
       query1.setParameter("id",id);
       return query1.getSingleResult();
    }

    @Override
    public Customer save(Customer customer) {
        Session session=null;
        Transaction transaction=null;
        try{
            session=sessionFactory.openSession();
            transaction=session.beginTransaction();
            Customer origin=findOne(customer.getId());
            origin.setName(customer.getName());
            origin.setEmail(customer.getEmail());
            origin.setAddress(customer.getAddress());
            session.saveOrUpdate(origin);
            transaction.commit();
            return origin;
        }catch (Exception e){
            e.printStackTrace();
            if(transaction!=null){
                transaction.rollback();
            }
        }finally {
            if(session!=null){
                session.close();
            }
        }
        return null;
    }

    @Override
    public List<Customer> save(List<Customer> customers) {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return customers.isEmpty();
    }

    @Override
    public List<Customer> findAll(List<Long> ids) {
        return Collections.emptyList();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long id) {
        customers.remove(id);
    }

    @Override
    public void delete(Customer customer) {
        customers.remove(customer);
    }

    @Override
    public void delete(List<Customer> customers) {
       this.customers.remove(customers);
    }

    @Override
    public void deleteAll() {
        customers.isEmpty();
    }
}
