package main.java.com.github.theLittleStone.components;

import main.java.com.github.theLittleStone.others.Condition;
import main.java.com.github.theLittleStone.others.NoAnswerException;

import java.util.ArrayList;

/**
 * Created by theLittleStone on 2023/4/27.
 */
public class FullCombination{
    public ArrayList<Unit> total = new ArrayList<>();
    public ArrayList<Unit> row;
    public ArrayList<Unit> column;
    public ArrayList<Unit> square;
    public Table belongs;

    public FullCombination(Combination row, Combination column, Combination square, Table belongs) {
        this.row = row.numbers;
        this.column = column.numbers;
        this.square = square.numbers;
        total.addAll(row.numbers);
        total.addAll(column.numbers);
        total.addAll(square.numbers);
        this.belongs = belongs;
    }

    @Override
    public String toString() {
        return "FullCombination{" +
                "total=" + total +
                '}';
    }

    //用于剔除一个单元格中不可能的取值
    public boolean stripImpossibleValue(Unit u){
        assert total.contains(u);
        if (Condition.Certain == u.condition){
            return false;
        }
        boolean isChanged = false;
        for (Unit number : total) {
            if (Condition.Certain == number.condition){
                if(u.uncertainValues.remove(number.fixedValue)){
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }

    //用于确定一个单元格在行/列/单元格中是否具有唯一的可能值, 如果有, 那这个可能值就是单元格的取值
    public int hasUniquePossibleValue(Unit u) throws NoAnswerException {
        assert total.contains(u);
        if(Condition.Certain == u.condition){
            return u.fixedValue;
        }
        //分别存放行/列/九宫格中决定的该单元格的值, 后续要比较. 不确定情况为0, 有确定值的情况是确定的值
        int rowUniqueCounter = 0;
        int alternativeRow = 0;
        int columnUniqueCounter = 0;
        int alternativeColumn = 0;
        int squareUniqueCounter = 0;
        int alternativeSquare = 0;
        //先搜索行
        for (int uncertainValue : u.uncertainValues) {
            boolean isUnique = true;
            for (Unit number : row) {
                if (Condition.Uncertain == number.condition && number != u) {
                    if (number.uncertainValues.contains(uncertainValue)) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                alternativeRow = uncertainValue;
                rowUniqueCounter++;
            }
        }
        if (rowUniqueCounter >= 2){
            throw new NoAnswerException();
        }
        //再搜索列
        for (int uncertainValue : u.uncertainValues) {
            boolean isUnique = true;
            for (Unit number : column) {
                if (Condition.Uncertain == number.condition && number != u) {
                    if (number.uncertainValues.contains(uncertainValue)) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                alternativeColumn = uncertainValue;
                columnUniqueCounter ++;
            }
        }
        if (columnUniqueCounter >= 2){
            throw new NoAnswerException();
        }
        //最后搜索九宫格
        for (int uncertainValue : u.uncertainValues) {
            boolean isUnique = true;
            for (Unit number : square) {
                if (Condition.Uncertain == number.condition && number != u) {
                    if (number.uncertainValues.contains(uncertainValue)) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                alternativeSquare = uncertainValue;
                squareUniqueCounter ++;
            }
        }
        if (squareUniqueCounter >= 2){
            throw new NoAnswerException();
        }

        //如果三个确定的值存在两两不同, 那么说明该数独(至少这种情况下)是无解的, 通过下面的判断决定是返回唯一值还是抛出异常
        if (alternativeRow == 0 ){
            if (alternativeColumn == 0){
                return alternativeSquare;
            }else if (alternativeSquare != 0 && alternativeColumn != alternativeSquare){
                throw new NoAnswerException();
            }else {
                return alternativeColumn;
            }
        }else{
            if (alternativeColumn == 0){
                if (alternativeSquare == 0){
                    return alternativeRow;
                }else if(alternativeSquare != alternativeRow){
                    throw new NoAnswerException();
                }else {
                    return alternativeRow;
                }
            }else {
                if (alternativeRow != alternativeColumn){
                    throw new NoAnswerException();
                }else if(alternativeSquare != 0 && alternativeSquare != alternativeRow){
                    throw new NoAnswerException();
                }else {
                    return alternativeRow;
                }
            }
        }
    }
}
