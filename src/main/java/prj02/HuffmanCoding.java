package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import HashTable.*;
import List.*;
import SortedList.*;
import Tree.*;


/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author Sebastian J. Caballero Diaz 802-19-2461 (Implementation)
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("input1.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/*We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it*/
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));
 
			/*If input file is empty just return an empty string, if not just extract the data*/
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return line;
	}

	/**
	 * This method takes an input string and makes a map for the frequency distribution,
	 * It loops through the input string's characters so that if it's a new character (not
	 * in map yet), it adds it to the map and puts this "count" to 1. Otherwise, of the character
	 * is already on the map, it puts in the map but instead summing 1 to the count.
	 * 
	 * @param inputString string that contains the message's characters/symbols
	 * @return Map with the symbol's frquency distribution
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		Map<String, Integer> characterFrequencyDistribution = new HashTableSC<>(new SimpleHashFunction<>()); // creating a new map 
		if(inputString.length() == 0) return null;
		for(int i = 0; i < inputString.length(); i++) {
			if(!characterFrequencyDistribution.containsKey(String.valueOf(inputString.charAt(i)))) { // if the key is not in the map
				characterFrequencyDistribution.put(String.valueOf(inputString.charAt(i)), 1); // add it and make the "count" 1
			}
			else { // it is in the map
				characterFrequencyDistribution.put(String.valueOf(inputString.charAt(i)), 1 + characterFrequencyDistribution.get(String.valueOf(inputString.charAt(i)))); // add it but also sum 1 to the "count"
			}
		}
		return characterFrequencyDistribution;
		
	}

	/**
	 * This method starts constructing the huffman tree after receiving the map with 
	 * the frequency distribution. It loops through this map's keys in order to add them 
	 * to the tree with their values. Then, the next for loop starts the process to
	 * construct the huffman tree.
	 * 
	 * @param fD Map with the character's frequency distribution
	 * @return Root node of the huffman tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {

		SortedLinkedList<BTNode<Integer, String>> rootNode = new SortedLinkedList<BTNode<Integer, String>>();

		if(fD.isEmpty()) return null;
		for(String i : fD.getKeys()) { // loop through the keys
			rootNode.add(new BTNode<Integer, String>(fD.get(i), i)); // add the binary tree nodes to the list
		}
		for (int i = 0; i < (fD.size() - 1); i++) { // loop through the map
			// creating the binary tree nodes
			BTNode<Integer, String> newNode = new BTNode<Integer, String>(); 
			BTNode<Integer, String> leftNode = rootNode.removeIndex(0);
			BTNode<Integer, String> rightNode = rootNode.removeIndex(0);
			
			// setting up the tree
			newNode.setLeftChild(leftNode);
			newNode.setRightChild(rightNode);
			newNode.setKey(leftNode.getKey() + rightNode.getKey());
			newNode.setValue(leftNode.getValue() + rightNode.getValue());
			
			rootNode.add(newNode);
		}
		
		return rootNode.removeIndex(0); 
	}

	/**
	 * Receives the rootNode of the huffman tree and maps each symbol to their Huffman code
	 * with a helper method huffman_code_helper. This helper method recursively puts in the
	 * frequency distribution map all the designated keys and values.
	 *
	 * @param huffmanRoot The root returned in the huffman_tree method
	 * @return A recursive call to huffman_code_helper returning the symbol map with their 
	 * huffman codes.
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		Map<String, String> symbolHCodeMap = new HashTableSC<String, String>(new SimpleHashFunction<>());
		return huffman_code_helper(symbolHCodeMap, huffmanRoot, ""); // recursive call to helper function
	}

	/**
	 * This method is responsible for encoding the message. Takes the map with the symbols and
	 * their huffman codes and the inputString, which contains the message to encode. Creates
	 * and encoded String by adding the symbols of the encodingMap to the own string in a for loop.
	 *
	 * @param encodingMap The huffman codes map
	 * @param inputString contains the message to be encoded
	 * @return The encoded string
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		/* TODO Encode String */
		String encodedString = ""; // make a new, empty string to add to it
		for (int i = 0; i < inputString.length(); i++) { // loop through the input string
			encodedString = encodedString + encodingMap.get(Character.toString(inputString.charAt(i))); // converting the characters in the inputString to string
			//and adding them to the encodedString
		}

		return encodedString; 
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with it's frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/
	
	/**
	 * Helper method for huffman_code. Does recursive calls to find the mapping for the symbol's
	 * codes (keeps assigning 0 if its a left child, 1 if its a right child) until the base case
	 * is reached (no more childs from left or right side. Returns the map with the character
	 * and its digit that will be used for encoding.
	 *
	 * @param symbolHCodeMap Map with the symbols and their corresponding huffman codes
	 * @param hRoot root node of the huffman tree
	 * @param digit that will be used for encoding
	 * @return The map with the symbols and their huffman code values.
	 */
	public static Map<String, String> huffman_code_helper(Map<String, String> symbolHCodeMap, BTNode<Integer, String> hRoot, String digit){
		if(hRoot.getLeftChild() == null && hRoot.getRightChild() == null) { //Base case, no more childs on the left side or right side
			symbolHCodeMap.put(hRoot.getValue(), digit);
			return symbolHCodeMap;
		}
		// in pre-order
		huffman_code_helper(symbolHCodeMap, hRoot.getLeftChild(), digit + "0"); // reactive call with the left child
		huffman_code_helper(symbolHCodeMap, hRoot.getRightChild(), digit + "1"); // reactive call with the right child
		
		return symbolHCodeMap;
	}
	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 *
	 * Used for output Purposes
	 *
	 * @param output - Encoded String
	 * @param lookupTable 
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { //Found it
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;
	}


}
