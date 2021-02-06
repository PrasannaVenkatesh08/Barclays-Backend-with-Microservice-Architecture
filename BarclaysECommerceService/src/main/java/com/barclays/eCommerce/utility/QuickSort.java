package com.barclays.eCommerce.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;


public class QuickSort {
	
	public String sortType = "" ;
	public String sortBy = "" ;
	public List<HashMap<String, Object>> list = null ;
	
	public QuickSort(List<HashMap<String, Object>> list, String sortBy, String sortType){
		this.sortBy = sortBy ;
		this.sortType = sortType ;
		this.list = list ;
	}
	
	public void sort() {
		this.quickSort(0, list.size()-1);
	}
	
	private void quickSort(int from, int to) {
	    if (from < to) {
	        int pivot = from;
	        int left = from + 1;
	        int right = to;
	        HashMap<String, Object> pivotObject = list.get(pivot);
	        int pivotValue = (int) pivotObject.get("");
	        while (left <= right) {
	            // left <= to -> limit protection
	            while (left <= to && pivotValue >= ( (int) list.get(left).get(sortBy) ) ) {
	                left++;
	            }
	            // right > from -> limit protection
	            while (right > from && pivotValue < ( (int) list.get(right).get(sortBy) ) ) {
	                right--;
	            }
	            if (left < right) {
	                Collections.swap(list, left, right);
	            }
	        }
	        Collections.swap(list, pivot, left - 1);
	        quickSort( from, right - 1); // <-- pivot was wrong!
	        quickSort( right + 1, to);   // <-- pivot was wrong!
	    }
	}
	
}
