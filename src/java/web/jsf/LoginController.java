/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jsf;

import com.oracle.bookstore.entities.Customer;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import web.jsf.util.JsfUtil;

/**
 *
 * @author tmshah
 */
@Named("loginController")
@SessionScoped
public class LoginController implements Serializable{
    
    @EJB
    private web.servicebeans.CustomerFacade ejbFacade;
    
    private Customer customer;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String login(){
        
        System.out.println("  "+username+"   --> "+password);
        Customer c = ejbFacade.findCustomerByUsername(username);
        if(c!= null && c.getPassword().equals(password)){
            this.customer = c;
            
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("incorrect.login"));
        }
        return "";
    }
    public String logout(){
        
        System.out.println("signout  "+username+"   --> "+password);
        this.setPassword(null);
        this.setUsername(null);
        this.customer = null;
        return "/index";
    }
    
}
