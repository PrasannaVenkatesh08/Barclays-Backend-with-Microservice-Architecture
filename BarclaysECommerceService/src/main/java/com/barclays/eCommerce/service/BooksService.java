package com.barclays.eCommerce.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.barclays.eCommerce.constants.BooksConstants;
import com.barclays.eCommerce.constants.PropertyConstants;
import com.barclays.eCommerce.utility.QuickSort;
import com.barclays.eCommerce.utility.SortComparator;


@Service
public class BooksService {
	
	@Autowired
	RestTemplate restTemplate ;
	
	@Autowired
	Environment environment ;
	
	private String emptyString = "" ;
	
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> fetchProducts(JSONObject reqObj) throws JSONException{
		HashMap<String, Object> mapOfProducts = new HashMap<>() ;
		String bookURL = environment.getProperty(PropertyConstants.BOOK_URL) ;
		String bookImageURL = environment.getProperty(PropertyConstants.BOOK_IMAGE_URL) ;		
		LinkedList<HashMap<String, Object>> listOfBooks = restTemplate.getForObject(bookURL, LinkedList.class) ;
		LinkedList<HashMap<String, Object>> listOfImages = restTemplate.getForObject(bookImageURL, LinkedList.class) ;
		int start = 0 ;
		List<HashMap<String, Object>> mismatchedData = new ArrayList<>() ;
		// Creating Regex Pattern for Search Functionality
		String searchValue = reqObj.getString(BooksConstants.SEARCH_VALUE).toLowerCase() ;
		Pattern searchPattern = Pattern.compile(".*"+searchValue+".*") ;
		String searchKey = reqObj.getString(BooksConstants.SEARCH_BY) ;
		for(HashMap<String, Object> book : listOfBooks) {
			if( start >= listOfImages.size() ) {
				start = 0 ;
			}
			
			// For Search Functionality
			Matcher matcher = null ;
			if(!searchValue.equals(emptyString)) {
				matcher = searchPattern.matcher(book.get(searchKey).toString().toLowerCase()) ;
			}
			if (book.get("average_rating") instanceof String || ( !searchValue.equals(emptyString) && !matcher.find() ) ) {
				mismatchedData.add(book);
			}
			// In-case Image server returns empty list
			String imagePath = ( start == listOfImages.size()) ? emptyString : (String) listOfImages.get(start).get(BooksConstants.IMAGE) ;
			book.put(BooksConstants.IMAGE_KEY, imagePath) ;
			start++ ;
		}
		listOfBooks.removeAll(mismatchedData);
		listOfBooks.sort(new SortComparator(reqObj.getString(BooksConstants.SORT_TYPE), reqObj.getString(BooksConstants.SORT_BY)));
		
		mapOfProducts.put(BooksConstants.LIST_OF_PRODUCTS, listOfBooks) ;
		mapOfProducts.put(BooksConstants.CURRENT_PAGE, 1 ) ;
		mapOfProducts.put(BooksConstants.TOTAL_PAGES, listOfBooks.size()) ;
		return mapOfProducts ;
	}
	
}
