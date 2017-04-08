package codility;

//find maxlength of substring of given string such that it does not contain a digit but contains atleast one upperCase char

class PasswordCheckTest {
    public static int solution(String S) {
        boolean hasUpperCaseChar = false;
        int lengthCounter = 0;
        int maxLength = 0;
        for (int i = 0; i < S.length(); i++) {
            if (Character.isDigit(S.charAt(i))) {
                if ((lengthCounter > maxLength) && (hasUpperCaseChar))
                    maxLength = lengthCounter;
                lengthCounter = 0;
                hasUpperCaseChar = false;
            } else {
                lengthCounter++;
                if (Character.isUpperCase(S.charAt(i))) {
                    hasUpperCaseChar = true;
                }
            }
        }
        if ((lengthCounter > maxLength) && (hasUpperCaseChar))
            maxLength = lengthCounter;
        return (maxLength == 0) ? -1 : maxLength;
    }

    public static void main(String[] args) {
        System.out.println(solution("a0Ba"));
    }
}