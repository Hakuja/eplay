/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import play.data.validation.CheckWith;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.MimeTypes;

import validators.*;

/**
 *
 * @author tutumi
 */
@Entity
public class AuctionItem extends Model {

    @Required
    public String title;
    public Date startDate;
    public Date endDate;
    @Required
    public Float deliveryCost;
    @Required
    public Float startBid;
    @Required
    public Float buyNowPrice;
    public Boolean buyNowEnabled;
    @Column(length = 4096)
    @Required
    @MinSize(20)
    public String description;
    public Integer viewCount = 0;
    @Transient
    @Required
    public Integer days;
    
    @Transient
    public File photoFile;
    public String photoFilename;
    public Blob photo;
    
    @ManyToOne
    public User createdBy;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    public List<Bid> bids = new ArrayList<Bid>();

    public void setDays(Integer days) {
        this.days = days;
        if (days != null) {
            startDate = new Date();
            endDate = new Date(System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000));
        }
    }
    
    public void setPhotoFile(File photoFile) throws FileNotFoundException {
		this.photoFile = photoFile;
		this.photoFilename = photoFile.getName();
		this.photo = new Blob();
		this.photo.set(new FileInputStream(photoFile), MimeTypes.getContentType(photoFilename));
	}
    
    public static List<AuctionItem> getMostPopular(int maxItems) {
        return find("endDate > ? order by viewCount DESC", new Date()).fetch(maxItems);
    }

    public static List<AuctionItem> getEndingSoon(int maxItems) {
        return find("endDate > ? order by endDate ASC", new Date()).fetch(maxItems);
    }

    public static SearchResults search(String search, Integer page) {
        String likeSearch = "%" + search + "%";
        long count = count("title like ? OR description like ? AND endDate > ? "
                + "order by endDate ASC ",
                likeSearch, likeSearch, new Date());
        List<AuctionItem> items = find("title like ? OR description like ? AND endDate > ? "
                + "order by endDate ASC ",
                likeSearch, likeSearch, new Date()).fetch(page, 5);

        return new SearchResults(items, count);
    }
    
    public static List<AuctionItem> recentlyAdded(int maxItems) {
    	return find("endDate > ? order by endDate ASC", new Date()).fetch(maxItems);
    }
    
    public void addBid(Float amount) {
    	bids.add(new Bid(amount));
    }
    
    public Float getCurrentTopBid() {
    	if (bids.size() == 0) {
    		return startBid;
    	} else {
    		return bids.get(bids.size() - 1).amount;
    	}
    }
    
    public Float getNextBidPrice() {
    	return getCurrentTopBid() + 2.50F;
    }
}
