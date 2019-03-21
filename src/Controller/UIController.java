package grp1Topic8;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UIController {
	
	@FXML
	TextField baseTwo;
	@FXML
	TextField binary;
	@FXML
	TextField hex;
	@FXML
	TextField eNotPrime;
	@FXML
	TextField sign;
	@FXML
	TextField typeOfSpecial;
	@FXML
	Button clearData;
	@FXML
	Button submit;
	
	protected String value;
	protected String exponent;
	protected String signBit;
	protected String posiOrNega;
	protected String RebuiltM;
	protected String ePrime;

	protected int exponentPRep = 0;
	protected int special = 0;
	protected int normalE = 0;
	
	public String getRebuiltM() {
		return RebuiltM;
	}
	public void setRebuiltM(String rebuiltM) {
		RebuiltM = rebuiltM;
	}
/*	public UIController() {}*/
	
	
	public void giveIn() {
		value = baseTwo.getText();
//		specialType = typeOfSpecial.getText();

			if(value.length() - value.replace(".", "").length() > 1) { //https://stackoverflow.com/questions/14092682/how-do-i-check-if-a-string-contains-the-same-letter-more-than-once
				binary.setText("11111111111 0000000000000000000000000000000000000000000000000010");
				special = 2;
				typeOfSpecial.setText("NaN");
				/*There should only be 1 . */System.out.print("A0129");
			}else {
				if(value.matches("[0-1, .]+") && (value.length() > 1)) { //https://stackoverflow.com/questions/10575624/java-string-see-if-a-string-contains-only-numbers-and-not-letters
					if(value.length() > 54) { //inclusive Mantissa and period
						binary.setText("11111111111 0000000000000000000000000000000000000000000000000010");
						special = 2;
						typeOfSpecial.setText("NaN");
						/*Mantissa should fit after conversion */System.out.print("A0130");
					}else {
						signValidation();
					}
					
				}else{
					binary.setText("11111111111 0000000000000000000000000000000000000000000000000010");
					special = 2;
					typeOfSpecial.setText("NaN");
					/*It is not the case that the input has 1's or 0's*/System.out.print("A0131");
				}
			}	
	}

	public void signValidation() {
		posiOrNega = sign.getText();
		
			if(posiOrNega.contains("+") && (posiOrNega.length() == 1)) {
				signBit = "0"; //sign bit SET!
				normalizer();
			}
			else if(posiOrNega.contains("-") && (posiOrNega.length() == 1)) {
				signBit = "1"; //sign bit SET!
				normalizer();
			}
			else {
				binary.setText("Invalid");
				hex.setText("Invalid");
				/*sign should only either be + or -  */
			}
	}
	
	public void normalizer() {
		value = baseTwo.getText();
		int localCounter = 0, localCounter2 = 0, exponentMover = 0, j = 0;
		boolean firstOneFound = false, periodFound = false;
		/*
		try {
			String[] tokens = value.split(".");
		
			if (tokens.length > 0)
				for(int i = 0; i < tokens.length; i++)
					ImportedData.add(tokens[i]);
		}catch(Exception F) {}
		*/
			/*
			 * Separate the string into all its characters (store as ArrayList)
			 */
		char[] eachBit = value.toCharArray();
		
		for(int i = 0; i < value.length(); i++) { //https://stackoverflow.com/questions/7970857/java-shifting-elements-in-an-array
			if((eachBit[i] == '1') && (firstOneFound == false)) { //http://www.tutorialspoint.com/java/lang/system_arraycopy.htm
				//set as index[0]
				localCounter = i;
				firstOneFound = true;
			}else if(eachBit[i] == '0') {
				//don't care
			}else if((eachBit[i] == '.') && (periodFound == false)){
				//move . to just after the first 1
				localCounter2 = i;
				periodFound = true;
			}
			j = i;
		}
		System.arraycopy(eachBit, localCounter2/*position of . */, eachBit, localCounter/*position of 1 */ + 1, eachBit.length); //trim leading 0's later
		
		
		exponentMover = localCounter - j;
		String rebuiltMantissa = new String(eachBit); //(Supposedly) rebuilds/reassembles the array of characters
			/* find the first 1 
			 * find the . 
			 * put that . in ArrayList index [1]
			 * (shift all elements' indeces so that the first 1 is at index[0])
			 * Stringbuilder to restore the String
			 * */
		exponent = eNotPrime.getText();
		exponentPRep = Integer.parseInt(exponent);
		normalE = exponentPRep - exponentMover;
		
		setRebuiltM(rebuiltMantissa);
		
		exponentValidation(normalE);
	}

	public void exponentValidation(int E) {
		exponent = eNotPrime.getText();
		exponentPRep = Integer.parseInt(exponent);
		
		if((exponentPRep < -1022) && (getRebuiltM().startsWith("1.000000000000000000000000000000000000000000000000000") && getRebuiltM().endsWith("0"))) { //Smallest Normalized Number (almost denormal)
			if((exponentPRep < -1074) && (getRebuiltM().startsWith("1.000000000000000000000000000000000000000000000000000") && getRebuiltM().endsWith("0"))) { //smallest denormalized (approaching 0)
				binary.setText("00000000000 0000000000000000000000000000000000000000000000000000");
				special = 1;
				typeOfSpecial.setText("0.0");
				/*Practically Zero*/
			}else {
				binary.setText("00000000000 0000000000000000000000000000000000000000000000000010");
				special = 4;
				typeOfSpecial.setText("Denormalized");
				/*denormalized*/
			}
		}else if((exponentPRep > 1023) && (getRebuiltM().startsWith("1.111111111111111111111111111111111111111111111111111") && getRebuiltM().endsWith("1"))) { //largest normalized number (approaching +/-Infinity)
			binary.setText("11111111111 0000000000000000000000000000000000000000000000000000");
			special = 3;
			typeOfSpecial.setText("Infinity");
			/* Infinity really */
		}else if((exponentPRep > -1023) && (getRebuiltM().startsWith("1.111111111111111111111111111111111111111111111111111") && getRebuiltM().endsWith("1"))) { //largest denormalized (pre-normal)
			process();
			/*don't care, it's normal anyway*/
		}else if(!(exponent.matches("[0-9]+"))){
			binary.setText("11111111111 0000000000000000000000000000000000000000000000000010");
			special = 2;
			typeOfSpecial.setText("NaN");
			/*invalid exponent*/
		}else {
			process();
		}
	}
	

	public void process() {
		System.out.println(exponent + " " + value);
		System.out.println(getRebuiltM());
		
	}
	
	public void witnessResult() {
		
//		binary.setText(signBit + arg1);
//		hex.setText(arg2);
	}
	
}
