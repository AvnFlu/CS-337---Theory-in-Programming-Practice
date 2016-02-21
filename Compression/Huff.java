//Michael Both
//mjb3299

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.*;

public class Huff{

	private static File file;
	private static BinTreeNode root;

	public static void main (String[] args) throws Exception {
	    //if no input was given through the command prompt, ask the user to enter a file name 
	    if(args.length == 0){
        	System.out.print("Please enter a filename to compress: ");
	                                
        	Scanner sc = new Scanner(System.in);
    	    file = new File(sc.next());
	        sc.close();
        }
        else file = new File(args[0]);
		FileReader in = new FileReader(file);

		//key - each unique character; value - number of times it appers in the file
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		//read everything from the input file
		int ch = 0;
		while((ch = in.read()) != -1){
			//adds the character to the map if it is not yet in it
			if(!map.containsKey(ch))
				map.put(ch,1);
			else
				map.put(ch, map.get(ch) + 1);
		}


		ArrayList<BinTreeNode> nodeList = new ArrayList<BinTreeNode>();
		Set<Integer> set = map.keySet();
		for(int key : set){
			nodeList.add(new BinTreeNode(key, map.get(key)));
		}

		//combines the elements of nodeList together into a binary tree
		//the smallest (lowest count), nodes will be strung together first
		while(nodeList.size() > 1) {
			BinTreeNode min1 = nodeList.get(0);
			BinTreeNode min2 = nodeList.get(1);
			
			//finds the smallest 2 elements in nodeList
			for(int i = 1; i < nodeList.size(); i++) {
				BinTreeNode n = nodeList.get(i);

				//replace min2 if this count is lower
				if(n.compareTo(min2) < 0)
					min2 = n;

				//swap min1 and min2 if this count is even lower than min1's
				if(min2.compareTo(min1) < 0) {
					min2 = min1;
					min1 = n;
				}
			}

			nodeList.remove(min1);
			nodeList.remove(min2);

			nodeList.add(new BinTreeNode(min1, min2));
		}
		
		
		//determine the size of the compressed file in bits
		root = nodeList.get(0);
		int fileSize = findSize(root, 0);

		//calculates the entropy
		int totalCount = root.getCount();
		double h = 0.00;
		for(int key : set){
			double prob = (double)map.get(key) / (double)totalCount;
			h -= prob * (Math.log(prob)/Math.log(2));
		}
		h *= map.size();
		
		//output
		System.out.println("Actual length of the file by Huffman coding is: " +fileSize+ " bits (" +(fileSize/8)+" bytes)");
		System.out.printf ("and minimum achievable is: %.2f bits\n", h);

		in.close();
		
	}
	
	//this recurisve function will return the size of the entire compressed file in bits
	public static int findSize(BinTreeNode n, int depth){
		if(n.getLeft() == null || n.getRight() == null)
			return depth * n.getCount();
		return findSize(n.getLeft(), depth + 1) + findSize(n.getRight(), depth + 1);
	}
}

