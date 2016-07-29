package web.jsf;

import com.oracle.bookstore.entities.Book;
import web.jsf.util.JsfUtil;
import web.jsf.util.PaginationHelper;
import web.servicebeans.BookFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import java.util.List;

@Named("bookController")
@SessionScoped
public class BookController implements Serializable {

    private Book current;
    private DataModel items = null;
    private DataModel cartItems = null;
    @EJB
    private web.servicebeans.BookFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String searchString;
    private DataModel searchResult = null;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public DataModel getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(DataModel searchResult) {
        this.searchResult = searchResult;
    }

    public BookController() {
    }
    
    public DataModel getCartItems() {
        if (cartItems == null) {
            cartItems = getPagination().createPageDataModel();
        }
        return cartItems;
    }

    public Book getSelected() {
        if (current == null) {
            current = new Book();
            selectedItemIndex = -1;
        }
        return current;
    }

    private BookFacade getFacade() {
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

    public String prepareBookmanager() {
        recreateModel();
        return "Bookmanager";
    }
    
    public String prepareView() {
        current = (Book) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Book();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BookCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    public String viewBook(Book b){
        try{
            current = b;
            System.out.println("current "+current);
//            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            System.out.println("selectedItemIndex "+selectedItemIndex);
        }catch(Exception e){
            System.out.println("My exception :: "+e);
        }
            return "View";
     }
   public String search(){
        try{
            String title = searchString;            
            if(!"".equalsIgnoreCase(title)){
                List searchResultList=getFacade().search("select b from Book b where b.title LIKE '%"+title+"%' or b.author LIKE '%"+title+"%' or b.publisher LIKE '%"+title+"%' or b.description LIKE '%"+title+"%' ");
                searchResult=new ListDataModel(searchResultList);                
            }else{
                searchResult = null;
            }
        }catch (Exception e) {
            System.out.println("Exception :: ");
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return null;
    }
    
    

    public String prepareEdit() {
        current = (Book) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BookUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Book) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroyFromBookManager() {
        current = (Book) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "Bookmanager";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BookDeleted"));
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

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Book getBook(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Book.class)
    public static class BookControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BookController controller = (BookController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bookController");
            return controller.getBook(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Book) {
                Book o = (Book) object;
                return getStringKey(o.getBookId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Book.class.getName());
            }
        }

    }
    
    public String addToCart() {
        if (getItems().getRowCount() > 0) {
            if (getItems().getRowIndex() >= 0) {
                current = (Book) getItems().getRowData();
                // send request to facade and save there.
                ejbFacade.saveBookInCart(current);
            }
            cartItems = ejbFacade.getCartBooksOfCustomer();
        }
        return "/cart/List";
    }
}
