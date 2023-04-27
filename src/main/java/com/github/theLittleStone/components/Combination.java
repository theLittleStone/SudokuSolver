package main.java.com.github.theLittleStone.components;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by theLittleStone on 2023/4/26.
 */
//9个数字(行, 列, 九宫格)的组合, 主要逻辑代码再FullCombination类中
public class Combination {
    public ArrayList<Unit> numbers;
    public Table belongs;

    public Combination(ArrayList<Unit> numbers, Table belongs) {
        this.numbers = numbers;
        this.belongs = belongs;
    }
}
