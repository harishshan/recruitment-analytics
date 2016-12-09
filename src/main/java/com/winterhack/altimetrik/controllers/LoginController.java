package com.winterhack.altimetrik.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.winterhack.altimetrik.dao.LoginDAO;
import com.winterhack.altimetrik.entity.Login;


@Controller
public class LoginController {

	@Autowired
	private LoginDAO loginDAO;
	
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			String username=request.getParameter("username")!=null?request.getParameter("username"):"";
			String password=request.getParameter("password")!=null?request.getParameter("password"):"";
			logger.info("Login Servlet Username:"+username+" Password:"+password);			
			Login login=new Login();
			login.setUsername(username);
			login.setPassword(password);
			login = loginDAO.findByUsernameAndPassword(login);
			if(login!=null){
				session.setAttribute("loginUser",login);
				return "home";
			} else {
				return "unauthorised";
			}					
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
			return "unauthorised";
		}
	}
	
	

	public LoginDAO getLoginDAO() {
		return loginDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}
}
