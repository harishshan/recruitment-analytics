package com.winterhack.altimetrik.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.winterhack.altimetrik.constants.CommonConstants;
import com.winterhack.altimetrik.dao.LoginDAO;
import com.winterhack.altimetrik.entity.Login;
import com.winterhack.altimetrik.entity.Role;
import com.winterhack.altimetrik.exception.UserAlreadyExistsException;
import com.winterhack.altimetrik.util.JsonUtil;


@Controller
public class LoginController {

	@Autowired
	private LoginDAO loginDAO;
	
	@Autowired
	private JsonUtil jsonUtil;
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			String username=request.getParameter("username")!=null?request.getParameter("username"):"";
			String password=request.getParameter("password")!=null?request.getParameter("password"):"";
			String rolename=request.getParameter("rolename")!=null?request.getParameter("rolename"):"";			
			logger.info("Login Servlet Username:"+username+" Password:"+password);			
			Login login=new Login();
			login.setUsername(username);
			login.setPassword(password);
			Role role = new Role();
			role.setRolename(rolename);
			login.setRole(role);
			login = loginDAO.findByUsernameAndPassword(login);
			if(login!=null){
				session.setAttribute("loginUser",login);
				switch(rolename){
					case CommonConstants.RECRUITER:
						return "home";
					case CommonConstants.EMPLOYEE:
						return "emp-home";
					case CommonConstants.ACCOUNTANT:	
						return "acc-home";
					default:
						return "unauthorised";
				}
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
	
	@RequestMapping(value="/adminlogin", method = RequestMethod.POST)
	public String adminLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			String username=request.getParameter("username")!=null?request.getParameter("username"):"";
			String password=request.getParameter("password")!=null?request.getParameter("password"):"";		
			logger.info("Login Servlet Username:"+username+" Password:"+password);			
			Login login=new Login();
			login.setUsername(username);
			login.setPassword(password);
			Role role = new Role();
			role.setRolename(CommonConstants.ADMIN);
			login.setRole(role);
			login = loginDAO.findByUsernameAndPassword(login);
			if(login!=null){
				session.setAttribute("loginUser",login);
				return "admin-home";
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
	
	@RequestMapping(value="/getUsers", method = RequestMethod.GET)
	public @ResponseBody JsonArray getUsers(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			List<Login> loginList= loginDAO.getLoginList();
			JsonArray jsonArray = new JsonArray();
			int i=0;
			for(Login login : loginList){
				JsonObject  jsonObject = new JsonObject();
				jsonObject.addProperty("index", i++);
				jsonObject.addProperty("username", login.getUsername());
				jsonObject.addProperty("password", login.getPassword());
				jsonObject.addProperty("emailid", login.getEmailId());
				jsonObject.addProperty("role", login.getRole().getRolename());
				jsonArray.add(jsonObject);
			}
			return jsonArray;
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
			return new JsonArray();
		}
	}
	@RequestMapping(value="/createUser", method = RequestMethod.POST)
	public String createUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		try
		{
			logger.info("Create Agent");
			String username=request.getParameter("username")!=null?request.getParameter("username"):"";
			String password=request.getParameter("password")!=null?request.getParameter("password"):"";
			String emailid=request.getParameter("emailid")!=null?request.getParameter("emailid"):"";
			String rolename=request.getParameter("rolename")!=null?request.getParameter("rolename"):"";
			Login login = new Login();
			login.setUsername(username);
			login.setPassword(password);
			login.setEmailId(emailid);
			Role role = new Role();
			role.setRolename(rolename);
			login.setRole(role);
			loginDAO.save(login);
			return "userCreationSuccessfull";
		} catch(UserAlreadyExistsException ex){
			return ex.getMessage();
		}
		catch(Exception e)
		{
			logger.error(e.toString(),e);
			return "error";
		}
	}

	public LoginDAO getLoginDAO() {
		return loginDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}
}
