package codility;

/**
 * Created by parag on 4/9/17.
 */
public class Tree {
    public int x;
    public Tree l;
    public Tree r;

    public Tree(int x, Tree l, Tree r) {
        this.x = x;
        this.l = l;
        this.r = r;
    }
}
