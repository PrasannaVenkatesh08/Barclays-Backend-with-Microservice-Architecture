package com.barclays.eCommerce;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.barclays.eCommerce.service.BooksService;

public class TestBooksService {

	@Autowired
	BooksService bookService ;
	
	JSONObject reqObject = null ;
	
	@Before
	public void setValue() throws JSONException {
		reqObject.put("sortBy", "average_rating") ;
		reqObject.put("sortType", "DESC") ;
		reqObject.put("searchValue", "Antigone") ;
		reqObject.put("searchBy", "title") ;
		reqObject.put("currentPage", 1) ;
		reqObject.put("itemsPerPage", 3) ;
	}
	
	@Test
	public void testfetchProducts() {
		HashMap<String, Object> rtrMap = new HashMap<>() ;
		try {
			rtrMap = bookService.fetchProducts(reqObject);
			Assertions.assertEquals(9, (int)rtrMap.get("totalRecords"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testfetchProducts_Scenerio2() {
		HashMap<String, Object> rtrMap = new HashMap<>() ;
		try {
			reqObject.put("searchValue", "") ;
			rtrMap = bookService.fetchProducts(reqObject);
			Assertions.assertEquals(10944, (int)rtrMap.get("totalRecords"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
