package web.jsf;

import com.oracle.bookstore.entities.Cart;
import com.oracle.bookstore.entities.Customer;
import web.jsf.util.JsfUtil;
import web.jsf.util.PaginationHelper;
import web.servicebeans.CartFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.enterprise.context.SessionScoped ;//for bean scoping
import javax.inject.Named; //for bean declaration

@Named("cartController")
@SessionScoped
public class CartController implements Serializable {

    private Cart current;

    @Inject
    private LoginController currentUser;

    public LoginController getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginController currentUser) {
        this.currentUser = currentUser;
    }

    private DataModel items = null;
    @EJB
    
    private web.servicebeans.CartFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    public CartController() {
    }

    public Cart getSelected() {
        if (current == null) {
            current = new Cart();
            current.setCartPK(new com.oracle.bookstore.entities.CartPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private CartFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Cart) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Cart();
        current.setCartPK(new com.oracle.bookstore.entities.CartPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getCartPK().setBookId(current.getBook().getBookId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CartCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Cart) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getCartPK().setBookId(current.getBook().getBookId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CartUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Cart) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CartDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }
    public String viewCustomerCart()
    {
      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "bookstorePU" );
      EntityManager entitymanager = emfactory.createEntityManager();
     // LoginController currentUser = new LoginController();
     Customer tempCustomer = currentUser.getCustomer();
     System.out.println("-----------------------------"+tempCustomer);
     Query query = entitymanager.createQuery( "SELECT c FROM Cart c where c.customerId = :vcurrentCustomer" );
     query.setParameter("vcurrentCustomer", tempCustomer);
     List<Cart> cartDetailsList = (List<Cart>)query.getResultList();
     System.out.println("-----------------------------"+tempCustomer);
     
     current = (Cart) getItems().getRowData() ;
     selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
     
     return "pages/cart/CustomerCart";
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Cart getCart(com.oracle.bookstore.entities.CartPK id) {
        return ejbFacade.find(id);
    }

    


    @FacesConverter(forClass = Cart.class)
    public static class CartControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CartController controller = (CartController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cartController");
            return controller.getCart(getKey(value));
        }

        com.oracle.bookstore.entities.CartPK getKey(String value) {
            com.oracle.bookstore.entities.CartPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.oracle.bookstore.entities.CartPK();
            key.setCartId(Integer.parseInt(values[0]));
            key.setBookId(values[1]);
            return key;
        }

        String getStringKey(com.oracle.bookstore.entities.CartPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCartId());
            sb.append(SEPARATOR);
            sb.append(value.getBookId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Cart) {
                Cart o = (Cart) object;
                return getStringKey(o.getCartPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cart.class.getName());
            }
        }

    }

}
