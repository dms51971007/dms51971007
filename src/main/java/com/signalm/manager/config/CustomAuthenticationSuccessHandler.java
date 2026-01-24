package com.signalm.manager.config;

import com.signalm.manager.model.User;
import com.signalm.manager.serv.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

	public CustomAuthenticationSuccessHandler(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {


		String userName = authentication.getName();


		User theUser = userService.findByUserName(userName);

		HttpSession session = request.getSession();
		session.setAttribute("user", theUser);
		session.setAttribute("filter", null);
		session.setMaxInactiveInterval(60*60*4);

		response.sendRedirect(request.getContextPath() + "/task/tasklist?user_id=-1&page=1");
	}

}
