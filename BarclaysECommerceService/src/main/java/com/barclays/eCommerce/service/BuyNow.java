package com.barclays.eCommerce.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.instamojo.wrapper.api.ApiContext;
import com.instamojo.wrapper.api.Instamojo;
import com.instamojo.wrapper.api.InstamojoImpl;
import com.instamojo.wrapper.exception.ConnectionException;
import com.instamojo.wrapper.exception.HTTPException;
import com.instamojo.wrapper.model.PaymentOrder;
import com.instamojo.wrapper.model.PaymentOrderResponse;

@Service
public class BuyNow {

	public void buy(HttpServletResponse res) {
		ApiContext context = ApiContext.create("RrkFuM2GQlCva9BNjsXxnpY2NYHiYUmcnIB9r6fW", "7Xn0pFTmnG1q6s1SRZwUIt6Ib7yr3fA7DHyFx7VoqR4tvlzRcTHTujuv6lLS2ZT1BHyLrkkbLZkggzmkZAVOaBZ8X2yO7i1RU0NF3HSJukmNYAVfho62UgTLblfaqXB9", ApiContext.Mode.LIVE);
		Instamojo api = new InstamojoImpl(context);

		/*
		 * Create a new payment order
		 */
		PaymentOrder order = new PaymentOrder();
		
		order.setName("Prasanna Venkatesh");
		order.setEmail("prasanavenkateshr@gmail.com");
		order.setPhone("8668174909");
		order.setCurrency("INR");
		order.setAmount(100D);
		order.setDescription("This is a test transaction.");
		order.setRedirectUrl("https://www.google.com/");
		order.setWebhookUrl("https://www.google.com/");
		order.setTransactionId(Integer.parseInt(Math.random()*1000+"")+"Berclays");

		try {
		    PaymentOrderResponse paymentOrderResponse = api.createPaymentOrder(order);
		    System.out.println(paymentOrderResponse.getPaymentOrder().getStatus());
		    try {
				res.sendRedirect(paymentOrderResponse.getPaymentOptions().getPaymentUrl());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (HTTPException e) {
		    System.out.println(e.getStatusCode());
		    System.out.println(e.getMessage());
		    System.out.println(e.getJsonPayload());

		} catch (ConnectionException e) {
		    System.out.println(e.getMessage());
		}
	}

}