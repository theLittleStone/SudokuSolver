package main.java.com.github.theLittleStone.others;

import java.util.HashSet;

/**
 * Created by theLittleStone on 2023/4/26.
 */
public class FullNumberHashSet extends HashSet<Integer> {
    public FullNumberHashSet() {
        for (int i = 1; i <= 9; i++) {
            add(i);
        }
    }
}
