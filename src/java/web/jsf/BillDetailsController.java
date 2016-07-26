package web.jsf;

import com.oracle.bookstore.entities.BillDetails;
import web.jsf.util.JsfUtil;
import web.jsf.util.PaginationHelper;
import web.servicebeans.BillDetailsFacade;

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

@Named("billDetailsController")
@SessionScoped
public class BillDetailsController implements Serializable {

    private BillDetails current;
    private DataModel items = null;
    @EJB
    private web.servicebeans.BillDetailsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public BillDetailsController() {
    }

    public BillDetails getSelected() {
        if (current == null) {
            current = new BillDetails();
            current.setBillDetailsPK(new com.oracle.bookstore.entities.BillDetailsPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private BillDetailsFacade getFacade() {
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
        current = (BillDetails) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new BillDetails();
        current.setBillDetailsPK(new com.oracle.bookstore.entities.BillDetailsPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getBillDetailsPK().setBookId(current.getBook().getBookId());
            current.getBillDetailsPK().setBillId(current.getBookBill().getBillId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BillDetailsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (BillDetails) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getBillDetailsPK().setBookId(current.getBook().getBookId());
            current.getBillDetailsPK().setBillId(current.getBookBill().getBillId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BillDetailsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (BillDetails) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BillDetailsDeleted"));
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

    public BillDetails getBillDetails(com.oracle.bookstore.entities.BillDetailsPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = BillDetails.class)
    public static class BillDetailsControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BillDetailsController controller = (BillDetailsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "billDetailsController");
            return controller.getBillDetails(getKey(value));
        }

        com.oracle.bookstore.entities.BillDetailsPK getKey(String value) {
            com.oracle.bookstore.entities.BillDetailsPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.oracle.bookstore.entities.BillDetailsPK();
            key.setBillId(Integer.parseInt(values[0]));
            key.setBookId(values[1]);
            return key;
        }

        String getStringKey(com.oracle.bookstore.entities.BillDetailsPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getBillId());
            sb.append(SEPARATOR);
            sb.append(value.getBookId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof BillDetails) {
                BillDetails o = (BillDetails) object;
                return getStringKey(o.getBillDetailsPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + BillDetails.class.getName());
            }
        }

    }

}
