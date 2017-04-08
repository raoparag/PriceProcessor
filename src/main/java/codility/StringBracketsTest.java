package codility;

//find string index K such that number of opening brackets in substring(1,K) is equal to number of closing brackets in substring (K+1,N)

class StringBracketsTest {
    public static int solution(String S) {
        if (S == null) return -1;
        long leftOpeningBrackets = 0;
        long rightClosingBrackets = 0;
        for (int i = 0; i < S.length(); i++) {
            if (S.charAt(i) == ')') rightClosingBrackets++;
        }
        if (leftOpeningBrackets == rightClosingBrackets) return 0;
        for (int i = 0; i < S.length(); i++) {
            if (S.charAt(i) == ')')
                rightClosingBrackets--;
            else if (S.charAt(i) == '(')
                leftOpeningBrackets++;
            if (leftOpeningBrackets == rightClosingBrackets) return i + 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Solution is " + solution(null));
    }
}