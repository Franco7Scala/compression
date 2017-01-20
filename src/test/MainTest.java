package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import compression.ArithmeticCompression;
import compression.Compressor;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		/*
		
		HashMap<Integer, Integer> xxx = new HashMap<>();
		xxx.put(22, 22);
		xxx.put(33, 22);
		xxx.put(44, 22);
		xxx.put(55, 22);
		
		
		for (int x : xxx.keySet()) {
			System.out.println("v: " + x);
		}
		
		System.out.println("-----");
		xxx.put(25, 22);
		
		for (int x : xxx.keySet()) {
			System.out.println("v: " + x);
		}
		
		
		System.out.println("-----");
	
		
		for (int x : xxx.keySet()) {
			System.out.println("v: " + x);
		}
		
		*/
		
		
		
		ArithmeticCompression c = new ArithmeticCompression();
		
		
		if (c.compress("/esercizi/compression/aaa.txt") ) {
			System.out.println("OOK");
		}
		else {
			System.out.println("MM");
		}
		
		
		if (c.decompress("/esercizi/compression/aaa.txt.ac", c.getProbabilities()) ) {
			System.out.println("OOK");
		}
		else {
			System.out.println("MM");
		}
		
		
		
		
		
		
		
		/*
		
		byte[] myByteArray = new byte[2];
		myByteArray[0] = 11;
		myByteArray[1] = 22;
		FileOutputStream fos = new FileOutputStream("/esercizi/pene.txt", true);
		fos.write(myByteArray);
		fos.close();
		
		
		
		
		FileInputStream fis = new FileInputStream("/esercizi/pene.txt");
		byte[] res = new byte[2];
		fis.read(res);
		fis.close();
		
		for (int c = 0; c < res.length; c++ ) {
			if (res[c]==myByteArray[c]) {
				System.out.println("PENE");
			}
			else {
				System.out.println("CAZZO");
			}
		}
		*/
		
	} 

}
