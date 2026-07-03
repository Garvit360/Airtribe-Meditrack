package com.airtribe.meditrack.interfaces;

public interface Searchable {
    boolean matchesSearchCriteria(String keyword);

    default  void displaySearchResult(){
        System.out.println("Search Result Found.");
    }
}
