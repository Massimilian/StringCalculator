package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private List<String> counting;
    private String status = "";
    private String allActions;
    private final List<Character> symbols = new ArrayList<>() {{
        add('+');
        add('-');
        add('*');
        add('/');
        add('(');
        add(')');
    }};

    public Calculator(String counting) {
        prepareArray(counting);
        buildAllActions();
    }

    private void buildAllActions() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < counting.size(); i++) {
            sb.append(counting.get(i));
            if (!(counting.get(i).charAt(0) == symbols.get(4))
                    || (i < counting.size() - 1 && !(counting.get(i + 1).charAt(0) == symbols.get(5)))) {
                sb.append(" ");
            }
        }
        sb.append("=");
        this.allActions = sb.toString();
    }

    private void prepareArray(String request) {
        this.counting = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < request.length(); i++) {
            if (Character.isDigit(request.charAt(i)) || symbols.contains(request.charAt(i))) {
                sb.append(request.charAt(i));
            }
        }
        request = sb.toString();
        sb = new StringBuilder();
        for (int i = 0; i < request.length(); i++) {
            if (!this.symbols.contains(request.charAt(i))) {
                sb.append(request.charAt(i));
            } else {
                if (!sb.isEmpty()) {
                    counting.add(sb.toString());
                }
                sb = new StringBuilder();
                counting.add(String.valueOf(request.charAt(i)));
            }
        }
        if (!sb.isEmpty()) {
            counting.add(sb.toString());
        }
        deleteZeroActions();
        deleteStartedClosingBrackets();
        checkLonelyBrackets();
        deleteZeros();
        minusNumbers();
        checkCounting();
        prepareBracketMultiply();
        countZeroDividing();
    }

    /**
     * Method deleted closed brackets from the start
     */
    private void deleteStartedClosingBrackets() {
        for (int i = 0; i < counting.size(); i++) {
            if (counting.get(i).charAt(0) == symbols.get(5)) {
                counting.remove(i);
                i--;
            }
            if (counting.get(i).charAt(0) == symbols.get(4)) {
                break;
            }
        }
    }

    /**
     * Method for prepare into bracked values when the multiply action is not in the array
     */
    private void prepareBracketMultiply() {
        for (int i = 0; i < counting.size() - 1; i++) {
            if (counting.get(i + 1).charAt(0) == symbols.get(4) && !symbols.contains(counting.get(i).charAt(0))) {
                counting.add(i + 1, String.valueOf(symbols.get(2)));
            }
            if (counting.get(i).charAt(0) == symbols.get(5) && !symbols.contains(counting.get(i + 1).charAt(0))) {
                counting.add(i + 1, String.valueOf(symbols.get(2)));
            }
        }
    }

    /**
     * Method for unnesessary zeros
     */
    private void deleteZeros() {
        for (int i = 0; i < counting.size(); i++) {
            counting.set(i, deleteZeros(counting.get(i)));
        }
    }

    /**
     * Makes numbers with negative value
     */
    private void minusNumbers() {
        for (int i = counting.size() - 3; i >= 0; i--) {
            if (symbols.contains(counting.get(i).charAt(0)) && counting.get(i + 1).charAt(0) == symbols.get(1)
                    && !symbols.contains(counting.get(i + 2).charAt(0))) {
                StringBuilder sb = new StringBuilder();
                sb.append(symbols.get(1)).append(counting.get(i + 2));
                counting.set(i + 1, sb.toString());
                counting.remove(i + 2);
                i--;
            }
        }
        if (counting.get(0).charAt(0) == symbols.get(1) && !symbols.contains(counting.get(1).charAt(0))) {
            StringBuilder sb = new StringBuilder();
            sb.append(symbols.get(1)).append(counting.get(1));
            counting.set(0, sb.toString());
            counting.remove(1);
        }
    }

    /**
     * Writes exception when divides on zero
     */
    private void countZeroDividing() {
        for (int i = 0; i < counting.size() - 1; i++) {
            if (counting.get(i).equals("/") && counting.get(i + 1).equals("0")) {
                status = " Impossible operation - cannot divide on '0'.";
                break;
            }
        }
    }

    /**
     * Deletes actions from start and end of the array
     */
    private void deleteZeroActions() {
        while (symbols.contains(counting.get(0).charAt(0)) && counting.get(0).length() == 1) {
            counting.remove(0);
        }
        while (symbols.contains(counting.get(counting.size() - 1).charAt(0)) && counting.get(0).length() == 1) {
            counting.remove(counting.size() - 1);
        }
    }

    /**
     * Method for delete double functions in the array
     */
    private void checkCounting() {
        for (int i = 0; i < counting.size() - 1; i++) {
            if (counting.get(i).length() == 1 && counting.get(i + 1).length() == 1) {
                if (symbols.contains(counting.get(i).charAt(0)) && symbols.contains(counting.get(i + 1).charAt(0))
                        && counting.get(i).charAt(0) != symbols.get(5) && counting.get(i + 1).charAt(0) != symbols.get(5)
                        && counting.get(i).charAt(0) != symbols.get(4) && counting.get(i + 1).charAt(0) != symbols.get(4)) {
                    counting.remove(i--);
                }
            }
        }
    }

    /**
     * Method for delete unnesessary zeros in the value
     *
     * @param s is a value
     * @return prepared value
     */
    private String deleteZeros(String s) {
        if (s.length() == 1) {
            return s;
        } else {
            if (s.startsWith("0")) {
                s = deleteZeros(s.substring(1, s.length()));
            }
        }
        return s;
    }

    /**
     * Main function of this program. Converting from brackets and counting
     *
     * @return result of counting in String format
     */
    public String act() {
        if (status.isEmpty()) {
            boolean allBracketsOpen = false;
            while (!allBracketsOpen) {
                allBracketsOpen = this.getAllBrackets(this.counting);
            }
            String result = this.count(this.counting);
            if (status.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.allActions);
                sb.append(" ");
                sb.append(this.makeBeautifulResult(result));
                sb.append(".");
                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(this.allActions);
                sb.append(status);
                return sb.toString();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.allActions);
        sb.append(status);
        return sb.toString();
    }

    /**
     * Method to find the value of into brackets arithmetic expression
     *
     * @param counting is array for counting
     * @return more easy array
     */
    private boolean getAllBrackets(List<String> counting) {
        boolean result = false;
        int startIndex = -1;
        int finishIndex = -1;
        for (int i = 0; i < counting.size(); i++) {
            if (counting.get(i).charAt(0) == this.symbols.get(4)) {
                startIndex = i;
                continue;
            }
            if (startIndex != -1) {
                if (counting.get(i).charAt(0) == this.symbols.get(5)) {
                    finishIndex = i;
                }
            }
            if (finishIndex != -1) {
                break;
            }
        }
        if (startIndex != -1 && finishIndex != -1) {
            ArrayList<String> part = new ArrayList<>();
            for (int i = 0; i < finishIndex - startIndex - 1; i++) {
                part.add(counting.get(i + startIndex + 1));
            }
            String semiresult = this.count(part);
            if (status.isEmpty()) {
                counting.set(startIndex, semiresult);
                for (int i = startIndex + 1; i < finishIndex + 1; i++) {
                    counting.remove(startIndex + 1);
                }
            }
        }
        result = startIndex == -1 || finishIndex == -1;
        return result;

    }

    /**
     * Method to check all brackets
     *
     * @return is number of brackets correct
     */
    private void checkLonelyBrackets() {
        int balanceBrackets = 0;
        for (int i = 0; i < counting.size(); i++) {
            if (counting.get(i).charAt(0) == symbols.get(4)) {
                balanceBrackets++;
            }
            if (counting.get(i).charAt(0) == symbols.get(5)) {
                balanceBrackets--;
            }
            if (balanceBrackets < 0) {
                break;
            }
        }
        if (balanceBrackets > 0) {
            status = " Implosible operation! There are opened and not closed brackets.";
        }
        if (balanceBrackets < 0) {
            status = " Impossible operation! Something is going wrong with closing brackets.";
        }
    }

    /**
     * Method for preparate array to beautiful string
     *
     * @param count
     * @return
     */
    private String makeBeautifulResult(String count) {
        if (count.contains(".")) {
            if (count.endsWith("0")) {
                count = count.substring(0, count.length() - 1);
                count = this.makeBeautifulResult(count);
            }
        }
        if (count.endsWith(".")) {
            count = count.substring(0, count.length() - 1);
        }
        if (count.startsWith("0") && count.charAt(1) != '.') {
            count = "0";
        }
        return count;
    }

    /**
     * Method for counting the value
     *
     * @param toCount is the part of main array for count
     * @return counted value
     */
    public String count(List<String> toCount) {
        this.countZeroDividing();
        if (toCount.size() < 3) {
            this.status = " Impossible operation! Cannot make this action.";
        }
        if (this.status.isEmpty()) {
            toCount = this.makeActions(toCount);
        }
        if (this.status.isEmpty()) {
            return toCount.get(0);
        } else {
            return this.status;
        }
    }

    /**
     * Method for make first and second arithmetic actions
     *
     * @param toCount is a part of array for count
     * @return the result of counting
     */
    private List<String> makeActions(List<String> toCount) {
        for (int i = 1; i < toCount.size() - 1; i = i + 2) {
            if (toCount.get(i).equals("*") || toCount.get(i).equals("/")) {
                toCount.set(i - 1, this.makeCount(toCount.get(i - 1), toCount.get(i), toCount.get(i + 1)));
                toCount.remove(i);
                toCount.remove(i);
                i = i - 2;
            }
        }
        for (int i = 1; i < toCount.size() - 1; i = i + 2) {
            toCount.set(i - 1, this.makeCount(toCount.get(i - 1), toCount.get(i), toCount.get(i + 1)));
            toCount.remove(i);
            toCount.remove(i);
            i = i - 2;
        }
        return toCount;
    }


    /**
     * Counting
     *
     * @param one    first parameter
     * @param action is what to do
     * @param two    second parameter
     * @return result of action of two parameters
     */
    private String makeCount(String one, String action, String two) {
        BigDecimal first = new BigDecimal(one);
        BigDecimal second = new BigDecimal(two);
        int act = symbols.indexOf(action.charAt(0));
        if (act == 0) {
            first = first.add(second);
        } else if (act == 1) {
            first = first.subtract(second);
        } else if (act == 2) {
            first = first.multiply(second);
        } else if (act == 3) {
            first = first.divide(second, 100, RoundingMode.HALF_UP);
        }
        return first.toString();
    }
}
