package com.datang.common.util;


import lombok.Data;

@Data
public class DataInfo {

        //汇总表的行
        private Integer targetRow;
        //明细表的列
        private String[] resourceColumns;
        //统计类型  求和、求平均
        private Type type;

        private String prevision;

        public DataInfo(int row,String column){
            this(row,column,Type.Average,"#.##%");
        }

    public DataInfo(int row,String column,String prevision){
        this(row,column,Type.Average,prevision);
    }

        public DataInfo(int row, String[] columns){
            this(row,columns,Type.Average,"#.##%");
        }

        public DataInfo(int row, String column, Type type,String prevision){
            this(row,new String[]{column},type,prevision);
        }



        public DataInfo(int row, String[] columns, Type type,String prevision){
            this.targetRow = row;
            this.resourceColumns = columns;
            this.type = type;
            this.prevision = prevision;
        }


        public enum Type{
            Sum,
            Average
        }


}
