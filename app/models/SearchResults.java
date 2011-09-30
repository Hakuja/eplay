/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

/**
 *
 * @author tutumi
 */
public class SearchResults {

    public List<AuctionItem> items;
    public Long count;

    public SearchResults(List<AuctionItem> items, Long count) {
        this.items = items;
        this.count = count;
    }
}
