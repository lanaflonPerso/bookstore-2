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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        // check if book is already in cart
        Query getBookQuery = getEntityManager().createQuery("select count(c) from Cart c where c.customerId.customerId = :custId and c.cartPK.bookId = :bookId");
        getBookQuery.setParameter("custId", 1);
        getBookQuery.setParameter("bookId", current.getBookId());
        int bookCount = ((Number)getBookQuery.getSingleResult()).intValue();
        if (bookCount == 0) {
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
    
    public DataModel getCartBooksOfCustomer() {
        // get all items of a user from database. Also add recently selected one and prepare list.
        Query query = getEntityManager().createQuery("select b from Book b, Cart c where b.bookId=c.cartPK.bookId and c.customerId.customerId= :cust");
        query.setParameter("cust", 1);
        List<Book> bookList = query.getResultList();
        return new ListDataModel(bookList);
    }
}
