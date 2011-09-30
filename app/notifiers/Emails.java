package notifiers;

import models.User;
import play.mvc.Mailer;

public class Emails extends Mailer {

	public static void welcome(User user) {
		setSubject("Welcome %s", user.firstname);
		addRecipient(user.email);
		setFrom("ePlay <eplay@localhost>");
		setContentType("text/plain; charset=ISO-2022-JP");
		setCharset("ISO-2022-JP");
		send(user);
	}
}
