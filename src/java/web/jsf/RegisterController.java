/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jsf;

import com.oracle.bookstore.entities.Customer;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import web.jsf.util.JsfUtil;
import web.servicebeans.CustomerFacade;

/**
 *
 * @author tmshah
 */
@Named("registerController")
@RequestScoped
@ManagedBean
public class RegisterController {
    
    public RegisterController(){
        this.customer = new Customer();
    }
    
    @EJB
    private web.servicebeans.CustomerFacade ejbFacade;
    
    private Customer customer;
    private boolean validName;
    private String confirmPassword;

    public CustomerFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(CustomerFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public boolean isValidName() {
        return validName;
    }

    public void setValidName(boolean validName) {
        this.validName = validName;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    
    public boolean validateUsername(){
        boolean isValid = false;
        Customer c = ejbFacade.findCustomerByUsername(customer.getUsername());
        System.out.println(".........................."+customer.getUsername());
        if(c != null){
            //this.customer = c;
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("user.already.exists"));
        } else {
            isValid = true;
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("username.valid"));
        }        
        System.out.println(".........................."+customer.getUsername()+isValid);
        return isValid;
    }
    
    public String register(){
        if(getConfirmPassword().equals(this.customer.getPassword())){
            
            ejbFacade.saveCustomer(customer);
        } else {
        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("password.notmatching"));
        }
        
        
        return "";
    }    
}
