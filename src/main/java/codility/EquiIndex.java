package codility;

//find index of array such that sum of elements on either side is equal (excluding the index element in both)

class EquiIndex {
    public int solution(int[] A) {
        // write your code in Java SE 8
        long sumLeft = 0;
        long sumRight = 0;
        for (int i = 1; i < A.length; i++) {
            sumRight = sumRight + A[i];
        }
        if (sumLeft == sumRight) return printSol(0);
        for (int i = 2; i <= A.length; i++) {
            sumLeft = sumLeft + A[i - 2];
            sumRight = sumRight - A[i - 1];
            if (sumLeft == sumRight) return printSol(i - 1);
        }
        return printSol(-1);
    }

    public int printSol(int a) {
        if (a == -1)
            System.out.println("No Equilibrium index");
        else
            System.out.println("Equilibrium index at " + a);
        return a;
    }
}