package gui.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import businessLogic.bean.FacadeBean;

public class LoginBean {

    private BLFacade facadeBL;
    private String password;
    private String username;
    
    public LoginBean() {
        this.facadeBL = FacadeBean.getBusinessLogic();
    }
    
    public String attemptLogin() {
        return "main";
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
