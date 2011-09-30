package controllers;

import play.*;

import play.data.validation.CheckWith;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Images;
import play.libs.Mail;
import play.libs.MimeTypes;
import play.mvc.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.google.gson.JsonObject;

import models.*;
import play.data.validation.Valid;
import play.db.jpa.Blob;
import validators.ImageValidator;
import static play.modules.pdf.PDF.*;

public class Application extends Controller {
	
	public static void recentlyAdded() {
		List<AuctionItem> items = AuctionItem.recentlyAdded(50);
		render(items);
	}
	
	public static void showPDF(Long id) {
		AuctionItem item = AuctionItem.findById(id);
		item.viewCount++;
		item.save();
		renderPDF(item);
	}

	public static void createAuctionItem() {
		if (session.get("user") == null) {
			Authenticate.login();
		}
		
		AuctionItem item = new AuctionItem();
		render(item);
	}

	public static void doCreateItem(@Valid AuctionItem item)
			throws IOException {
		
		if (validation.hasErrors()) {
			System.out.println(validation.errorsMap());
			render("@createAuctionItem", item);
		}
		
		// set the user based on the logged in user
		item.createdBy = Authenticate.getLoggedInUser();

		item.save();
		show(item.id);
	}

	public static void show(Long id) {
		System.out.printf("=== id: %d\n", id);
		List<AuctionItem> items = AuctionItem.findAll();
		for (AuctionItem item : items) {
			System.out.printf("--- id: %d\n", item.id);
		}
		AuctionItem item = AuctionItem.findById(id);
		item.viewCount++;
		item.save();
		render(item);
	}

	public static void index() {
		List<AuctionItem> mostPopular = AuctionItem.getMostPopular(5);
		List<AuctionItem> endingSoon = AuctionItem.getEndingSoon(5);
		render(mostPopular, endingSoon);
	}

	public static void search(String search, Integer page) {
		validation.required(search).message(
				"You must enter something to search for");
		if (validation.hasErrors()) {
			render();
		}

		page = page == null ? 1 : page;

		SearchResults results = AuctionItem.search(search, page);
		render(results, page, search);
	}

	public static void showImage(Long id) {
		AuctionItem item = AuctionItem.findById(id);
		response.setContentTypeIfNotSet(item.photo.type());
		renderBinary(item.photo.get());
	}
	
	public static void addBid(Long id, Float amount) {
		AuctionItem item = AuctionItem.findById(id);
		item.addBid(amount);
		item.save();
	}
	
	public static void newBids(Long id) {
		// count new bids
		long newBids = Bid.count("from AuctionItem a join a.bids as b " +
				"where a.id = ? AND b.date > ?", id, request.date);
		
		// wait if needed
		if (newBids == 0) {
			await("1s");
		}
		
		// return the JSON output of the new bids
		AuctionItem item = AuctionItem.findById(id);
		
		JsonObject json = new JsonObject();
		json.addProperty("next", item.getNextBidPrice());
		json.addProperty("top", item.getCurrentTopBid());
		renderJSON(json.toString());
	}
	
	public static void mail() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setFrom("koichi.tsutsumi@gmail.com");
		email.addTo("hakuja@gmail.com");
		email.setSubject("メールのテスト");
		email.setMsg("メールの本文です。本文です。");
		email.setContent("メールの本文です。本文です。", "text/plain; charset=ISO-2022-JP");
		email.setCharset("ISO-2022-JP");
		Mail.send(email);
	}
}
