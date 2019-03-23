package Controller;

import javafx.beans.binding.*;
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

        convert.setDisable(true);

        inputSign.setEditable(true);
        beforeDot.setEditable(true);
        afterDot.setEditable(true);
        exponentSign.setEditable(true);
        exponent.setEditable(true);
        binaryAnswer.setText("");
        hexAnswer.setText("");

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(inputSign.textProperty(),
                        beforeDot.textProperty(),
                        afterDot.textProperty(),
                        exponentSign.textProperty(),
                        exponent.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (inputSign.getText().isEmpty()
                        || beforeDot.getText().isEmpty()
                        || afterDot.getText().isEmpty()
                        || exponentSign.getText().isEmpty()
                        || exponent.getText().isEmpty());
            }
        };

        convert.disableProperty().bind(bb);

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
            if (toggleGroup.getSelectedToggle() == qNaNButton){
                inputSign.setEditable(false);
                beforeDot.setEditable(false);
                afterDot.setEditable(false);
                exponentSign.setEditable(false);
                exponent.setEditable(false);
                beforeDot.setText("");
                afterDot.setText("");
                inputSign.setText("");
                exponentSign.setText("");
                exponent.setText("");
                String output = convert(true, exponentSign.getText());
                binaryAnswer.setText(output);
                hexAnswer.setText(getHex(output));
            }
            else if (toggleGroup.getSelectedToggle() == sNaNButton){
                inputSign.setEditable(false);
                beforeDot.setEditable(false);
                afterDot.setEditable(false);
                exponentSign.setEditable(false);
                exponent.setEditable(false);
                beforeDot.setText("");
                afterDot.setText("");
                inputSign.setText("");
                exponentSign.setText("");
                exponent.setText("");
                String output = convert(false, exponentSign.getText());
                binaryAnswer.setText(output);
                hexAnswer.setText(getHex(output));
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
            String output = convert(inputSign.getText(), beforeDot.getText(), afterDot.getText(), exponent.getText(), exponentSign.getText());
            binaryAnswer.setText(output);
            hexAnswer.setText(getHex(output));
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

    public String convert(boolean isQNaN, String exponentSign) {
        StringBuilder output;
        if(exponentSign.equals("-"))
            output = new StringBuilder("1");
        else
            output = new StringBuilder("0");
        output.append("  ");
        for (int i = 0; i < 11; i++) output.append('1');
        output.append("  ");
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

        if(exponentSign.equals("-")){
            newExponent *= (-1);
        }

        int ePrime = newExponent + 1023;
        System.out.println(ePrime);
        if (ePrime >= 2047) { // Infinity
            output.append("  ");
            for (int i = 0; i < 11; i++) output.append('1');
            output.append("  ");
            for (int i = 0; i < 52; i++) output.append('0');
            return output.toString();
        }
        if (ePrime <= 0) { // Denormalized
            output.append("  ");
            for (int i = 0; i < 11; i++) output.append('0');
            output.append("  ");
            for (int i = 0; i < 52; i++) output.append('0');
            output.append('1');
            return output.toString();
        }
        String ePrimeBinary = Integer.toBinaryString(ePrime);
        for (int i = 0; i < 11 - ePrimeBinary.length(); i++) output.append('0');
        output.append("  ");
        output.append(ePrimeBinary);
        output.append("  ");
        output.append(newMantissa.toString());

        while (output.length() < 64) {
            output.append('0');
        }

        return output.toString();
    }

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

        String[] result = new String[binaryCharArray.length];
        for (int i = 0; i < binaryCharArray.length; i++) {
            result[i] = Integer.toHexString(Integer.parseInt(binaryCharArray[i], 2));
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
