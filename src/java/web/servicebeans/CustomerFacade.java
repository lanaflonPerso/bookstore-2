/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servicebeans;

import com.oracle.bookstore.entities.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hsavalia
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }
    
    public Customer findCustomerByUsername(String username){
        
    Customer c = null;    
                List results = em.createNamedQuery("Customer.findByUsername")
    .setParameter("username", username)
    .getResultList();
                
                if(!results.isEmpty() && results.size() == 1)
                    c = (Customer) results.get(0);
        return c;
    }
    
}
