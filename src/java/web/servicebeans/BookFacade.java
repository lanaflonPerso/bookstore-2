/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servicebeans;

import com.oracle.bookstore.entities.Book;
import com.oracle.bookstore.entities.Cart;
import com.oracle.bookstore.entities.CartPK;
import com.oracle.bookstore.entities.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hsavalia
 */
@Stateless
public class BookFacade extends AbstractFacade<Book> {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public BookFacade() {
        super(Book.class);
    }
    
    public void saveBookInCart(Book current) {
        // create cart object and save it
        Cart objCart = new Cart();
        CartPK objCartPK = new CartPK(1, current.getBookId());
        objCart.setCartPK(objCartPK);
        Customer objCustomer = new Customer();
        objCustomer.setCustomerId(1);
        objCart.setCustomerId(objCustomer);
        objCart.setQuantity(10);
        objCart.setStatus(1);
        em.persist(objCart);
    }
    
}
