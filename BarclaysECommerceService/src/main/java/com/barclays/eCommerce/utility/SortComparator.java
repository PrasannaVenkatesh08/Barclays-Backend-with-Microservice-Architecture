package com.barclays.eCommerce.utility;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.barclays.eCommerce.constants.BooksConstants;
import com.barclays.eCommerce.constants.PropertyConstants;

public class SortComparator implements Comparator<HashMap<String, Object>> {

	String sortType = "" ;
	String sortBy = "" ;
	
	public SortComparator(String sortType, String sortBy) {
		// TODO Auto-generated constructor stub
		this.sortBy = sortBy ;
		this.sortType = sortType ;
	}
	
	@Override
	public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
		// TODO Auto-generated method stub
		
		try{
			double value1 = Double.parseDouble(o1.get(sortBy).toString()) ; 
			double value2 = Double.parseDouble(o2.get(sortBy).toString()) ;
		if( sortType.equals(PropertyConstants.DESCENDING) ) {
			if(value1 > value2) {
				return -1 ;
			}else if(value1 == value2) {
				return 0 ;
			}else {
				return 1 ;
			}
		}else {
			if(value1 < value2) {
				return -1 ;
			}else if(value1 == value2) {
				return 0 ;
			}else {
				return 1 ;
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0 ;
	}

}
