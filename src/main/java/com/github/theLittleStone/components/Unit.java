package main.java.com.github.theLittleStone.components;

import main.java.com.github.theLittleStone.others.Condition;
import main.java.com.github.theLittleStone.others.FullNumberHashSet;
import main.java.com.github.theLittleStone.others.Location;
import main.java.com.github.theLittleStone.others.NoAnswerException;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by theLittleStone on 2023/4/26.
 */
public class Unit {
    public Condition condition;
    public int fixedValue;
    public HashSet<Integer> uncertainValues;
    public Location location;
    public Table belongs;

    //深复制方法
    public Unit(Unit u, Table belongs){
        condition = u.condition;
        fixedValue = u.fixedValue;
        if (condition == Condition.Uncertain){
            uncertainValues = new HashSet<>();
            uncertainValues.addAll(u.uncertainValues);
        }else {
            fixedValue = u.fixedValue;
        }
        location = new Location(u.location);
        this.belongs = belongs;
    }

    //未确定格子的构造方法
    public Unit(Location location, Table belongs) {
        condition = Condition.Uncertain;
        uncertainValues = new FullNumberHashSet();
        this.location = location;
        this.belongs = belongs;
    }

    //确定格子的构造方法
    public Unit(Location location, Table belongs, int fixedValue) {
        condition = Condition.Certain;
        this.fixedValue = fixedValue;
        this.uncertainValues = null;
        this.location = location;
        this.belongs = belongs;
    }

    //更新一次格子, 如果更新了值返回true, 否则返回false
    public boolean update() throws NoAnswerException {
        boolean isChanged = false;
        if (Condition.Uncertain == condition){
            //先更新可能的值
            FullCombination combinations = belongs.getFullCombination(location);
            isChanged = combinations.stripImpossibleValue(this);
            //再判断有没有可能值是行/列/九宫格中的唯一值
            int result = combinations.hasUniquePossibleValue(this);
            if(result != 0){
                condition = Condition.Certain;
                fixedValue = result;
                uncertainValues = null;
                isChanged = true;
            }
        }
        return isChanged;
    }

    public int getUncertainNumberCount(){
        if (condition == Condition.Certain){
            return 1;
        }else {
            return uncertainValues.size();
        }
    }

    public ArrayList<Unit> getAllPossibleUnits(){
        ArrayList<Unit> target = new ArrayList<>();
        for (Integer uncertainValue : uncertainValues) {
            target.add(new Unit(location, belongs, uncertainValue));
        }
        return target;
    }

    @Override
    public String toString() {
        if (Condition.Certain == condition) {
            return ""+fixedValue;
        }else {
            return "*";
        }
    }
}
