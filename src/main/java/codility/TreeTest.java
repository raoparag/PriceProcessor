package codility;

import java.util.ArrayList;

//traverse a binary tree to find a path with maximum distinct integers

class TreeTest {
    private static long maxCounter = 0;
    private static ArrayList<Integer> pathNumbers = new ArrayList<>();


    public int solution(Tree T) {
        maxCounter = 0;
        btraverse(T);
        return (int) maxCounter;
    }

    private void btraverse(Tree T) {
        pathNumbers.add(T.x);
        if (T.l != null) {
            btraverse(T.l);
        }
        if (T.r != null) {
            btraverse(T.r);
        }
        if ((T.l == null) && (T.r == null)) {
            long distinctNumbersCount = pathNumbers.stream().distinct().count();
            if (distinctNumbersCount > maxCounter) maxCounter = distinctNumbersCount;
        }
        pathNumbers.remove(pathNumbers.size()-1);
    }


    public static void main(String[] args) {
        Tree t41 = new Tree(6, null, null);
        Tree t42 = new Tree(7, null, null);
        Tree t31 = new Tree(4, t41, null);
        Tree t21 = new Tree(5, t31, null);
        Tree t32 = new Tree(1, null, t42);
        Tree t33 = new Tree(6, null, null);
        Tree t22 = new Tree(6, t32, t33);
        Tree t1 = new Tree(4, t21, t22);
        System.out.println("Solution is " + (new TreeTest()).solution(t1));
    }

//    class codility.Tree {
//        public int x;
//        public codility.Tree l;
//        public codility.Tree r;
//
//        public codility.Tree(int x, codility.Tree l, codility.Tree r) {
//            this.x = x;
//            this.l = l;
//            this.r = r;
//        }
//    }
}

//            4
//        5            6
//     4           1        6
//   6                7