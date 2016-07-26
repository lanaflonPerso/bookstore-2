/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servicebeans;

import com.oracle.bookstore.entities.BillDetails;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hsavalia
 */
@Stateless
public class BillDetailsFacade extends AbstractFacade<BillDetails> {

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BillDetailsFacade() {
        super(BillDetails.class);
    }
    
}
