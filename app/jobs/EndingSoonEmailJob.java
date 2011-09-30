package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.mail.SimpleEmail;

import models.AuctionItem;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import play.libs.Mail;

@On("0 45 * * * ?")
public class EndingSoonEmailJob extends Job {

	@Override
	public void doJob() throws Exception {
		Logger.info("=== EndingSoonEmailJob#doJob ===");
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.MINUTE, 90);
		Date hourAndHalf = cal.getTime();
		cal.add(Calendar.MINUTE, -60);
		Date halfHour = cal.getTime();

		List<AuctionItem> endingSoon = AuctionItem.find(
				"endDate < ? AND endDate > ?", hourAndHalf, halfHour).fetch();
		
		for (AuctionItem item : endingSoon) {
			String mailBody = "Your auction for '" + item.title +
					"' will be ending soon";
			
			SimpleEmail email = new SimpleEmail();
			email.setFrom("notification@eplay.com");
			email.addTo(item.createdBy.email);
			email.setSubject("Ending Soon");
			email.setContent(mailBody, "text/plain; charset=ISO-2022-JP");
			email.setCharset("ISO-2022-JP");
			Mail.send(email);
		}
	}
}
