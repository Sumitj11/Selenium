package com.HybridFramework.sampleTest;

import java.util.Hashtable;

public class HashTable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
Hashtable<String,String> table = new Hashtable<String, String>();

table.put("A", "Apple");
table.put("B", "Ball");
table.put("C", "Cat");

System.out.println("Value of A is " + table.get("A"));
System.out.println("Value of B is " + table.get("B"));		
System.out.println("Value of C is " + table.get("C"));
		
	}

}
