package main.java.com.github.theLittleStone.components;

import main.java.com.github.theLittleStone.others.Condition;
import main.java.com.github.theLittleStone.others.Location;
import main.java.com.github.theLittleStone.others.NoAnswerException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by theLittleStone on 2023/4/26.
 */

//棋盘类, 管理整个数独, 并负责数独的求解
public class Table {
    //存放9x9的Unit类
    public Unit[][] table = new Unit[9][9];

    //从二维数组构造一个Table
    public Table(int[][] table) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int t = table[i][j];
                if ( 1 <= t && 9 >= t) {
                    this.table[i][j] = new Unit(new Location(i, j), this, t);
                }else {
                    this.table[i][j] = new Unit(new Location(i, j), this);
                }
            }
        }
    }

    //深复制一个Table
    public Table(Table t){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                table[i][j] = new Unit(t.table[i][j], this);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Unit[] units : table) {
            StringBuilder s = new StringBuilder();
            for (Unit unit : units) {
                s.append(unit.toString()).append(" ");
            }
            sb.append(s).append("\r\n");
        }
        return sb.toString();
    }

    public void replaceUnit(Unit u){
        int row = u.location.row;
        int column = u.location.column;
        table[row][column] = u;
    }

    //获取对应一行
    public Combination getRowCombination(Location location){
        int row = location.row;
        ArrayList<Unit> tmp = new ArrayList<>(Arrays.asList(table[row]).subList(0, 9));
        return new Combination(tmp, this);
    }

    //获取对应一列
    public Combination getColumnCombination(Location location){
        int column = location.column;
        ArrayList<Unit> tmp = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            tmp.add(table[i][column]);
        }
        return new Combination(tmp, this);
    }

    //获取对应九宫格
    public Combination getSquireCombination(Location location){
        int row = location.row;
        int rawGroup = row / 3;
        int column = location.column;
        int columnGroup = column / 3;

        ArrayList<Unit> tmp = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tmp.add(table[3*rawGroup + i][3*columnGroup + j]);
            }
        }
        return new Combination(tmp, this);
    }

    //一横一竖一九宫格加起来, 决定了一个格子还能填什么值
    public FullCombination getFullCombination(Location location) {
        return new FullCombination(getRowCombination(location),
                getColumnCombination(location), getSquireCombination(location), this);
    }

    //判断这个棋盘是不是已经完成
    public boolean isAllCertain(){
        for (Unit[] units : table) {
            for (Unit unit : units) {
                if (Condition.Uncertain == unit.condition){
                    return false;
                }
            }
        }
        return true;
    }

    //对所有元素遍历更新一次
    public int updateAll() throws NoAnswerException {
        int counter = 0;
        for (Unit[] units : table) {
            for (Unit unit : units) {
                if (unit.update()) {
                    counter++;
                }
            }
        }
        return counter;
    }

    //获取所有未确定元素中未定值最少的一个, 如果多个元素有相同的未定值, 获取第一个
    public Unit getLeastChoiceUnit(){
        Unit target = null;
        int leastNumber = 10;
        for (Unit[] units : table) {
            for (Unit unit : units) {
                if(Condition.Uncertain == unit.condition){
                    if (null == target){
                        leastNumber = unit.getUncertainNumberCount();
                        target = unit;
                    }else{
                        if (leastNumber >= unit.getUncertainNumberCount() ){
                            leastNumber = unit.getUncertainNumberCount();
                            target = unit;
                        }
                    }
                }
            }
        }
        return target;
    }

    //到了需要猜值的时候, 对每一个可能的取值都生成一个新的棋盘, 默认选取可能值最小的一个格子试值
    public ArrayList<Table> getPossibleTables(){
        ArrayList<Table> target = new ArrayList<>();
        Unit u = getLeastChoiceUnit();
        for (Unit possibleUnit : u.getAllPossibleUnits()) {
            Table t = new Table(this);
            t.replaceUnit(possibleUnit);
            target.add(t);
        }
        return target;
    }

    //先循环更新每个格子直到没有确定解, 然后用递归方式去试值
    public Table solve(){
        try {
            while (updateAll() != 0){}
            if (isAllCertain()) {
                return this;
            }
        }catch (NoAnswerException e) {
            return null;
        }
        ArrayList<Table> list = getPossibleTables();
        for (Table t : list) {
            Table result = t.solve();
            if (null != result){
                return result;
            }
        }
        return null;
    }

}
