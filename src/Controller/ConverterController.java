package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javax.swing.*;

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
                if (!"+-".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
        exponentSign.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"+-".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
        exponent.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });

        convert.setOnAction(event -> {
            String output = convert(inputSign.getText(), beforeDot.getText(), afterDot.getText(), exponent.getText());
            binaryAnswer.setText(output);
        });

        clear.setOnAction(event -> {

        });

//        ActionListener qNaNListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                inputSign.setEditable(false);
//                beforeDot.setEditable(false);
//                afterDot.setEditable(false);
//                exponentSign.setEditable(false);
//                exponent.setEditable(false);
//                String output = convert(true);
//                binaryAnswer.setText(output);
//            }
//        };
//        qNaNButton.addActionListener(qNaNListener);
//
//        ActionListener sNaNListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                inputSign.setEditable(false);
//                beforeDot.setEditable(false);
//                afterDot.setEditable(false);
//                exponentSign.setEditable(false);
//                exponent.setEditable(false);
//                String output = convert(true);
//                binaryAnswer.setText(output);
//            }
//        };
//        sNaNButton.addActionListener(sNaNListener);
    }

    @FXML
    protected void clear(ActionEvent event) {
        inputSign.setEditable(true);
        beforeDot.setEditable(true);
        afterDot.setEditable(true);
        exponentSign.setEditable(true);
        exponent.setEditable(true);
        binaryAnswer.setText("");
        hexAnswer.setText("");
    }
    public String convert(boolean isQNaN) {
        StringBuilder output = new StringBuilder("0");
        for (int i = 0; i < 11; i++) output.append('1');
        if (isQNaN) output.append('1');
        else output.append('0');
        for (int i = 0; i < 51; i++) output.append('1');
        return output.toString();
    }

    public String convert(String sign, String whole, String mantissa, String exponent) {
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
}
