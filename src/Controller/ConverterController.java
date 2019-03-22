package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.event.*;

public class ConverterController {
    @FXML
    TextField inputSign;
    @FXML
    TextField beforeDot;
    @FXML
    TextField afterDot;
    @FXML
    TextField exponentSign;
    @FXML
    TextField exponent;
    @FXML
    RadioButton sNaNButton;
    @FXML
    RadioButton qNaNButton;
    @FXML
    Button clear;
    @FXML
    Button convert;
    @FXML
    Label binaryAnswer;
    @FXML
    Label hexAnswer;

    public void initialize(){
        inputSign.setEditable(true);
        beforeDot.setEditable(true);
        afterDot.setEditable(true);
        exponentSign.setEditable(true);
        exponent.setEditable(true);
        binaryAnswer.setText("");
        hexAnswer.setText("");

        beforeDot.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"01".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });

        afterDot.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"01".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });

        inputSign.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"+-".contains(keyEvent.getCharacter()) || inputSign.getText().length()>=1) {
                    keyEvent.consume();
                }
            }
        });

        exponentSign.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"+-".contains(keyEvent.getCharacter()) || exponentSign.getText().length()>=1) {
                    keyEvent.consume();
                }
            }
        });


        exponent.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789".contains(keyEvent.getCharacter()) || exponent.getText().length()>=4) {
                    keyEvent.consume();
                }
            }
        });

        ToggleGroup toggleGroup = new ToggleGroup();

        sNaNButton.setToggleGroup(toggleGroup);
        qNaNButton.setToggleGroup(toggleGroup);

        RadioButtonSelectionHandler b1Selection = new RadioButtonSelectionHandler(sNaNButton);
        sNaNButton.setOnMousePressed(b1Selection.getMousePressed());
        sNaNButton.setOnMouseReleased(b1Selection.getMouseReleased());

        RadioButtonSelectionHandler b2Selection = new RadioButtonSelectionHandler(qNaNButton);
        qNaNButton.setOnMousePressed(b2Selection.getMousePressed());
        qNaNButton.setOnMouseReleased(b2Selection.getMouseReleased());

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) ->
        {
            if ((toggleGroup.getSelectedToggle() == qNaNButton) || (toggleGroup.getSelectedToggle() == sNaNButton)){
                inputSign.setEditable(false);
                beforeDot.setEditable(false);
                afterDot.setEditable(false);
                exponentSign.setEditable(false);
                exponent.setEditable(false);
                binaryAnswer.setText("");
                hexAnswer.setText("");
                beforeDot.setText("");
                afterDot.setText("");
                inputSign.setText("");
                exponentSign.setText("");
                exponent.setText("");
            }
            else {
                inputSign.setEditable(true);
                beforeDot.setEditable(true);
                afterDot.setEditable(true);
                exponentSign.setEditable(true);
                exponent.setEditable(true);
                binaryAnswer.setText("");
                hexAnswer.setText("");
                beforeDot.setText("");
                afterDot.setText("");
                inputSign.setText("");
                exponentSign.setText("");
                exponent.setText("");
            }
        });

        convert.setOnAction(event -> {
            if(toggleGroup.getSelectedToggle() == qNaNButton){
                String output = convert(true);
                binaryAnswer.setText(output);
                hexAnswer.setText(getHex(output));
            }
            else if(toggleGroup.getSelectedToggle() == sNaNButton){
                String output = convert(false);
                binaryAnswer.setText(output);
                hexAnswer.setText(getHex(output));
            }
            else{
                String output = convert(inputSign.getText(), beforeDot.getText(), afterDot.getText(), exponent.getText(), exponentSign.getText());
                binaryAnswer.setText(output);
                hexAnswer.setText(getHex(output));
            }
        });

        clear.setOnAction(event -> {
            inputSign.setEditable(true);
            beforeDot.setEditable(true);
            afterDot.setEditable(true);
            exponentSign.setEditable(true);
            exponent.setEditable(true);
            binaryAnswer.setText("");
            hexAnswer.setText("");
            beforeDot.setText("");
            afterDot.setText("");
            inputSign.setText("");
            exponentSign.setText("");
            exponent.setText("");
            sNaNButton.setSelected(false);
            qNaNButton.setSelected(false);
        });

    }

    public String convert(boolean isQNaN) {
        StringBuilder output = new StringBuilder("0");
        for (int i = 0; i < 11; i++) output.append('1');
        if (isQNaN) output.append('1');
        else output.append('0');
        for (int i = 0; i < 51; i++) output.append('1');
        return output.toString();
    }

    public String convert(String sign, String whole, String mantissa, String exponent, String exponentSign) {
        StringBuilder output = new StringBuilder();
        if (sign.equals("+")) output.append("0");
        else output.append("1");
        int newExponent = Integer.parseInt(exponent);
        StringBuilder newMantissa = new StringBuilder();
        int wholeIndex = whole.indexOf('1'); // index of 1 in whole
        int mantissaIndex = mantissa.indexOf('1'); // index of 1 in mantissa
        if (wholeIndex != -1) {
            newExponent += whole.length() - wholeIndex - 1;
            newMantissa.append(whole.substring(wholeIndex + 1));
            newMantissa.append(mantissa);
        } else {
            if (mantissaIndex == -1) { // 0.0
                for (int i = 0; i < 63; i++) output.append('0');
                return output.toString();
            } else {
                newExponent -= mantissaIndex + 1;
                newMantissa.append(mantissa.substring(mantissaIndex + 1));
            }
        }
        if(exponentSign == "-")
            newExponent *= -1;
        int ePrime = newExponent + 1023;
        System.out.println(ePrime);
        if (ePrime >= 2047) { // Infinity
            for (int i = 0; i < 11; i++) output.append('1');
            for (int i = 0; i < 52; i++) output.append('0');
            return output.toString();
        }
        if (ePrime <= 0) { // Denormalized
            for (int i = 0; i < 62; i++) output.append('0');
            output.append('1');
            return output.toString();
        }
        String ePrimeBinary = Integer.toBinaryString(ePrime);
        for (int i = 0; i < 11 - ePrimeBinary.length(); i++) output.append('0');
        output.append(ePrimeBinary);
        output.append(newMantissa.toString());

        while (output.length() < 64) {
            output.append('0');
        }
        return output.toString();
    }

//    private static String binaryToHex(String binary) {
//        int decimalValue = 0;
//        int length = binary.length() - 1;
//        for (int i = 0; i < binary.length(); i++) {
//            decimalValue += Integer.parseInt(binary.charAt(i) + "") * Math.pow(2, length);
//            length--;
//        }
//        return decimalToHex(decimalValue);
//    }
//    private static String decimalToHex(int decimal){
//        String hex = "";
//        while (decimal != 0){
//            int hexValue = decimal % 16;
//            hex = toHexChar(hexValue) + hex;
//            decimal = decimal / 16;
//        }
//        return hex;
//    }
//
//    private static char toHexChar(int hexValue) {
//        if (hexValue <= 9 && hexValue >= 0)
//            return (char)(hexValue + '0');
//        else
//            return (char)(hexValue - 10 + 'A');
//    }

    public String getHex(String b) {
        int index = 0;
        String binary = b;
        String[] binaryCharArray = new String[binary.length() / 4];
        for (int i = 0; i < binary.length() / 4; i++) {
            binaryCharArray[i] = "";
            for (int j = index; j < index + 4; j++) {
                binaryCharArray[i] += binary.charAt(j);
            }
            index += 4;
        }
        return binaryToHex(binaryCharArray);
    }

    public String binaryToHex(String[] bin) {
        String[] result = new String[bin.length];
        for (int i = 0; i < bin.length; i++) {
            result[i] = Integer.toHexString(Integer.parseInt(bin[i], 2));
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            output.append(result[i].toUpperCase());
        }
        return output.toString();
    }

    public class RadioButtonSelectionHandler  {

        private boolean selected = false;

        private RadioButton radioButton;

        public RadioButtonSelectionHandler(RadioButton radioButton) {
            this.radioButton = radioButton;
        }

        EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(radioButton.isSelected())
                    selected = true;
            }
        };

        EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selected)
                    radioButton.setSelected(false);
                selected = false;
            }
        };

        public EventHandler<MouseEvent> getMousePressed() {
            return mousePressed;
        }

        public EventHandler<MouseEvent> getMouseReleased() {
            return mouseReleased;
        }
    }
}
