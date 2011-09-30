package controllers;

import notifiers.Emails;
import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;

public class Authenticate extends Controller {
	
	public static void login() {
		render();
	}
	
	public static void doLogin(String username, String password) {
		if (User.isValidLogin(username, password)) {
			doLoginLogic(username);
			Application.index();
		} else {
			validation.addError("username", "ユーザ名かパスワードが違います");
			render("@login", username);
		}
	}

	private static void doLoginLogic(String username) {
		session.put("user", username);
	}
	
	public static void logout() {
		session.remove("user");
		Application.index();
	}
	
	public static void register() {
		User user = new User();
		render(user);
	}
	
	public static void doRegister(@Valid User user) {
		// if there are errors, redisplay the registration form,
		// otherwise save the user
		if (!user.validateAndSave()) {
			render("@register", user);
		}
		
		// if no errors, log the user in and redirect to the index page
		doLoginLogic(user.username);
		Emails.welcome(user);
		Application.index();
	}
	
	public static User getLoggedInUser() {
		return User.find("byUsername", session.get("user")).first();
	}
}
