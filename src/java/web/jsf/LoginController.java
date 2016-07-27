/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author tmshah
 */
@Named("loginController")
@SessionScoped
@ManagedBean
public class LoginController {
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
    
    public String login(){
        
        System.out.println("  "+username+"   --> "+password);
        
        return "";
    }
    public String logout(){
        
        System.out.println("  "+username+"   --> "+password);
        this.setPassword(null);
        this.setUsername(null);
        return "/index";
    }
    
}
