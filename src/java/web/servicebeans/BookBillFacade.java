/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servicebeans;

import com.oracle.bookstore.entities.BillDetails;
import com.oracle.bookstore.entities.BookBill;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import web.jsf.LoginController;

/**
 *
 * @author hsavalia
 */
@Stateless
public class BookBillFacade extends AbstractFacade<BookBill> {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;
    
    
    @Inject
    LoginController objLoginController;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public LoginController getObjLoginController() {
        return objLoginController;
    }

    public void setObjLoginController(LoginController objLoginController) {
        this.objLoginController = objLoginController;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookBillFacade() {
        super(BookBill.class);
    }
    
    public List<BillDetails> viewPurchaseHistory()
    {
      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "bookstorePU" );
      EntityManager entitymanager = emfactory.createEntityManager();
      List<BillDetails> billDetailsList= new ArrayList<BillDetails>();
     
     Query query = entitymanager.createQuery( "SELECT B.billId FROM BookBill B where B.customerId = :vcurrentCustomer" );
     query.setParameter("vcurrentCustomer", getObjLoginController().getCustomer());
     try
     {
     List<Integer> billIdList = (List<Integer>)query.getResultList();
     
     System.out.println("-----------------------------"+billIdList);
     
     TypedQuery<BillDetails> strQuery = null;
     strQuery = entitymanager.createQuery("SELECT B FROM BillDetails B where B.bookBill.billId IN  :inclList ", BillDetails.class);
     strQuery.setParameter("inclList", billIdList);
      billDetailsList = (List<BillDetails>)strQuery.getResultList();
     }
     catch(Exception e) {
         System.out.println("No data found");
     }
    return billDetailsList;
    }
    
}
