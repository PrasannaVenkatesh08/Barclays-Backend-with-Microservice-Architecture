package com.barclays.eCommerce.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.eCommerce.constants.BooksConstants;
import com.barclays.eCommerce.constants.PropertyConstants;
import com.barclays.eCommerce.service.BooksService;
import com.barclays.eCommerce.service.BuyNow;
import com.instamojo.wrapper.api.ApiContext;
import com.instamojo.wrapper.api.Instamojo;
import com.instamojo.wrapper.api.InstamojoImpl;
import com.instamojo.wrapper.exception.ConnectionException;
import com.instamojo.wrapper.exception.HTTPException;
import com.instamojo.wrapper.model.PaymentOrder;
import com.instamojo.wrapper.model.PaymentOrderResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class BooksController {
	
	@Autowired
	Environment environment ;
	
	@Autowired
	BooksService bookService ;
	
	@Autowired
	BuyNow buyNow ;
	
	@GetMapping("/fetchHeaders")
	@HystrixCommand(fallbackMethod = "fallbackGetSubHeaders")
	public HashMap<String, Object> getSubHeaders(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, Object> subHeaders = new HashMap<>() ;
		String[] arrOfHeaders = environment.getProperty(PropertyConstants.SUB_HEADERS).split(",") ; 
		subHeaders.put(BooksConstants.HEADER_NAME, arrOfHeaders) ;
		subHeaders.put(BooksConstants.SELECTED_HEADER, environment.getProperty(PropertyConstants.SUB_HEADER_SELECTED)) ;
		return subHeaders ;
	}
	
	public HashMap<String, Object> fallbackGetSubHeaders(HttpServletRequest request, HttpServletResponse response){
		return new HashMap<String,Object>() ;
	}
	
	@PostMapping("/fetchProducts")
	public HashMap<String, Object> fetchProducts(@RequestBody String requestString, HttpServletRequest request, HttpServletResponse response) throws JSONException{
		HashMap<String, Object> mapOfProducts = new HashMap<>() ;
		JSONObject requestObject = new JSONObject(requestString) ;
		mapOfProducts = bookService.fetchProducts(requestObject) ;
		return mapOfProducts ;
	}
	
	public HashMap<String, Object> fallbackFetchProducts(@RequestBody String requestString, HttpServletRequest request, HttpServletResponse response){
		return new HashMap<String,Object>() ;
	}
	
	@PostMapping("/placeOrder")
	public HashMap<String, Object> placeOrder(@RequestBody String reqString, HttpServletRequest request, HttpServletResponse response) throws JSONException{
		HashMap<String, Object> rtrMap = new HashMap<>() ;
		String clinetID = environment.getProperty(PropertyConstants.CLIENT_ID);
		String clinetSecretID = environment.getProperty(PropertyConstants.CLIENT_SECRET_ID);
		ApiContext context = ApiContext.create(clinetID, clinetSecretID, ApiContext.Mode.LIVE);
		Instamojo api = new InstamojoImpl(context);
		JSONObject reqObject = new JSONObject(reqString) ;
		/*
		 * Create a new payment order
		 */
		PaymentOrder order = new PaymentOrder();
		
		String redirectURL = environment.getProperty(PropertyConstants.REDIRECT_URL);
		String webHookURL = environment.getProperty(PropertyConstants.WEB_HOOK_URL);
		order.setName(reqObject.getString(BooksConstants.NAME));
		order.setEmail(reqObject.getString(BooksConstants.EMAIL));
		order.setPhone(reqObject.getString(BooksConstants.MOBNUMBER));
		order.setCurrency(BooksConstants.CURRENCY);
		order.setAmount(reqObject.getDouble(BooksConstants.AMOUNT));
		order.setDescription("This is a test transaction.");
		order.setRedirectUrl(redirectURL);
		order.setWebhookUrl(webHookURL);
		order.setTransactionId(UUID.randomUUID().toString());

		try {
		    PaymentOrderResponse paymentOrderResponse = api.createPaymentOrder(order);
		    System.out.println(paymentOrderResponse.getPaymentOrder().getStatus());
		    
		    rtrMap.put(BooksConstants.URL, paymentOrderResponse.getPaymentOptions().getPaymentUrl() ) ;
		    rtrMap.put(BooksConstants.MESSAGE, "" ) ;
		} catch (HTTPException e) {
		    System.out.println(e.getStatusCode());
		    System.out.println(e.getMessage());
		    System.out.println(e.getJsonPayload());
		    rtrMap.put(BooksConstants.MESSAGE, e.getMessage() ) ;
		    rtrMap.put(BooksConstants.STATUS, e.getStatusCode()) ;
		} catch (ConnectionException e) {
		    System.out.println(e.getMessage());
		    rtrMap.put(BooksConstants.MESSAGE, e.getMessage() ) ;
		}
		return rtrMap ;
	}
	
	@PostMapping("/responseFromInstaMojo")
	public void responseFromServer(@RequestBody String reqString) throws JSONException {
		JSONObject reqObj = new JSONObject(reqString) ;
		System.out.println(reqObj);
	}
	
}
