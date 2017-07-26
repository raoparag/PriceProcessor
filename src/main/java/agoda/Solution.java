package agoda;

/**
 * Created by parag on 5/7/17.
 */
public class Solution {
    static void StairCase(int n) {

        for (int i = 1; i <= n; i++) {
            for (int j = i; j < n; j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < i; j++) {
                System.out.print("#");
            }
            System.out.println();
        }
    }

    static int summation(int[] numbers) {
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum = sum + numbers[i];
        }
        return sum;
    }


    static String[] prefixToPostfix(String[] prefixes) {
        String[] postfix = new String[prefixes.length];
        for (int i = 0; i < prefixes.length; i++) {
            postfix[i] = prefixToPostfix(prefixes[i]);
        }
        return postfix;
    }

    static String prefixToPostfix(String prefix) {
        String[] splitPrefix = prefix.split("");
        String[] opPrecedence = {"/", "*", "+", "-"};
        for (String anOpPrecedence : opPrecedence) {
            for (int i = 0; i < splitPrefix.length; i++) {
                if (splitPrefix[i].equalsIgnoreCase(anOpPrecedence)) {
                    splitPrefix[i] = splitPrefix[i + 1] + splitPrefix[i + 2] + splitPrefix[i];
                    for (int j = i + 1; j < splitPrefix.length - 2; j++) {
                        splitPrefix[j] = splitPrefix[j + 1];
                        splitPrefix[j + 1] = splitPrefix[j + 2];
                    }
                }
            }
        }
        return splitPrefix[0];
    }

    public static void main(String[] args) {
        System.out.println(prefixToPostfix("+1*23"));
    }
}
