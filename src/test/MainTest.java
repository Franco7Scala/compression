package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
import java.util.HashMap;

import javax.swing.SortingFocusTraversalPolicy;

import compression.ArithmeticCompression;
import compression.Compressor;
import compression.LempelZiv78;
import utilities.Support;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		
		
		
		BitSet[] states = new BitSet[(int) Math.pow(2, 3)];
		for ( int i = 0; i < states.length; i ++ ) {
			states[i] = BitSet.valueOf(new long[] { i }).get(0, 3);
			
			for ( int a =0; a < states[i].size(); a ++ ) {
				System.out.print( (states[i].get(a)? 1 : 0) + " ");
			}
			System.out.println("");
		}
		
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
		
		
		
//		ArithmeticCompression c = new ArithmeticCompression();
//		
//		
//		if (c.compress("/esercizi/compression/aaa.txt") ) {
//			System.out.println("OOK");
//		}
//		else {
//			System.out.println("MM");
//		}
//		
//		
//		if (c.decompress("/esercizi/compression/aaa.txt.ac", c.getProbabilities()) ) {
//			System.out.println("OOK");
//		}
//		else {
//			System.out.println("MM");
//		}
		
		/*
		LempelZiv78 c = new LempelZiv78();
		if (c.compress("/esercizi/compression/test.tush") ) {
			System.out.println("OOK");
		}
		else {
			System.out.println("MM");
		}
		if (c.decompress("/esercizi/compression/test.tush.lz78", null) ) {
			System.out.println("OOK");
		}
		else {
			System.out.println("MM");
		}*/
		
//		boolean err = false;
//		for ( int i = 0; i < c.a.size(); i ++ ) {
//			/*if ( c.a.get(i) != c.b.get(i) ) {
//				System.out.println("a: " + c.a.get(i) +  " b: " + c.b.get(i) + " at index " + i);
//				err = true;
//			}*/
//			if (i > 20000) 
//			System.out.println("a: " + c.a.get(i) + " at index " + i);
//		}
//		
//		
//		for ( int i = 0; i < c.b.size(); i ++ ) {
//			/*if ( c.a.get(i) != c.b.get(i) ) {
//				System.out.println("a: " + c.a.get(i) +  " b: " + c.b.get(i) + " at index " + i);
//				err = true;
//			}*/
//			if (i > 20000) 
//			System.out.println("b: " + c.b.get(i) + " at index " + i);
//		}
//		
//		
//		for ( int i = 0; i < c.a.size(); i ++ ) {
//			if ( c.a.get(i) != c.b.get(i) ) {
//				System.out.println("BOTH a: " + c.a.get(i) +  " b: " + c.b.get(i) + " at index " + i);
//				err = true;
//			}
//		}
//		
//		
//		if (err) System.out.println("ERROR");
//		
		
		
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
		
		/*
		byte[] a = {33, 33};
		
		System.out.print("A: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a[0], i) + " ");
		}
		System.out.print(" | ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a[1], i) + " ");
		}
		System.out.print("\n");
		
		byte[] b = {13, 52};
		System.out.print("B: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(b[0], i) + " ");
		}
		System.out.print(" | ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(b[1], i) + " ");
		}
		System.out.print("\n");
		
		byte[] r = Support.sumBytesArrayBitPerBit(a, b);
		
		System.out.print("R: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(r[0], i) + " ");
		}
		System.out.print(" | ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(r[1], i) + " ");
		}
		System.out.print("\n");
		*/
		/*
		a = Support.shiftByteArray(a, 4);
		
		System.out.print("A: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a[0], i) + " ");
		}
		System.out.print(" | ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a[1], i) + " ");
		}
		System.out.print(" | ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a[2], i) + " ");
		}
		System.out.print("\n");
		
		*/
		/*
		byte a = 3;
		System.out.print("A: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a, i) + " ");
		}
		System.out.print("\n");
		
		a = (byte)(a >> 3);
		
		System.out.print("A: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(Support.getBitValue(a, i) + " ");
		}
		System.out.print("\n");
		*/
		/*
		byte a = 100;
		byte b = 55;
		System.out.print("A: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(getBitValue(a, i) + " ");
		}
		System.out.print("\n");
		
		System.out.print("B: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(getBitValue(b, i) + " ");
		}
		System.out.print("\n");
		
		
		byte r = sumBytesBitPerBit(a, b);
		
		System.out.print("R: ");
		for ( int i = 0; i < 8; i ++ ) {
			System.out.print(getBitValue(r, i) + " ");
		}
		System.out.print("\n");
		*/
	} 
	
	
	

	


	
	
	
	
	
	

}
