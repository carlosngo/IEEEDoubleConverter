package Controller;

public class ConverterController {

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

    public static void main(String[] args) {
//        String binary = new ConverterController().convert("+", "0", "001011", "-1020");
//        String binary = new ConverterController().convert(false);
//        System.out.println(binary.length());
//        System.out.println(binary);

    }
}
