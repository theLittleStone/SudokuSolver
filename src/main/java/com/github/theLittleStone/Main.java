package main.java.com.github.theLittleStone;

import main.java.com.github.theLittleStone.components.Table;
import main.java.com.github.theLittleStone.others.Location;
import main.java.com.github.theLittleStone.others.NoAnswerException;

/**
 * Created by theLittleStone on ${DATE}.
 */
public class Main {
    static int[][] tmp = {   {0,3,0,8,0,0,0,4,6},
                             {0,0,2,1,0,7,8,0,0},
                             {5,0,0,0,0,6,0,0,0},
                             {0,2,0,0,0,0,5,7,0},
                             {6,0,0,0,0,0,0,0,0},
                             {7,1,0,5,0,8,0,0,0},
                             {0,0,0,0,2,0,3,0,8},
                             {0,0,0,0,0,3,0,0,4},
                             {0,8,0,0,0,0,0,0,1}};
    public static void main(String[] args) throws NoAnswerException {

        Table t = new Table(tmp);
        System.out.println(t);

        //效率统计
        Runtime r = Runtime.getRuntime();
        r.gc();
        long startMemory = r.freeMemory();
        long startTime = System.currentTimeMillis();

        //解数独
        Table result = t.solve();

        //效率统计
        long endTime = System.currentTimeMillis();
        long endMemory = r.freeMemory();

        System.out.println(result);

        System.out.println("占用内存：" + (startMemory - endMemory) / 1024 + "KByte");
        System.out.println("运行时间：" + (endTime - startTime) + "ms");
    }
}