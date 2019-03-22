import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.swing.*;
import javafx.event.EventHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBase;

public class Controller {
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
    JButton sNaNButton;
    @FXML
    JButton qNaNButton;
    @FXML
    JButton clear;
    @FXML
    JButton convert;
    @FXML
    JLabel binaryAnswer;
    @FXML
    JLabel hexAnswer;

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

        ActionListener converterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = convert(inputSign.getText(), beforeDot.getText(), afterDot.getText(), exponent.getText());
                binaryAnswer.setText(output);
            }
        };
        convert.addActionListener(converterListener);

        ActionListener clearListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputSign.setEditable(true);
                beforeDot.setEditable(true);
                afterDot.setEditable(true);
                exponentSign.setEditable(true);
                exponent.setEditable(true);
                binaryAnswer.setText("");
                hexAnswer.setText("");
            }
        };
        clear.addActionListener(clearListener);

        ActionListener qNaNListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputSign.setEditable(false);
                beforeDot.setEditable(false);
                afterDot.setEditable(false);
                exponentSign.setEditable(false);
                exponent.setEditable(false);
                String output = convert(true);
                binaryAnswer.setText(output);
            }
        };
        qNaNButton.addActionListener(qNaNListener);

        ActionListener sNaNListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputSign.setEditable(false);
                beforeDot.setEditable(false);
                afterDot.setEditable(false);
                exponentSign.setEditable(false);
                exponent.setEditable(false);
                String output = convert(true);
                binaryAnswer.setText(output);
            }
        };
        sNaNButton.addActionListener(sNaNListener);
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
