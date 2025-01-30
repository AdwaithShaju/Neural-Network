import java.util.*;
import java.io.*;
import java.lang.Math;

class value{

    ArrayList<value> toposot = new ArrayList<>();
    ArrayList<value> prev = new ArrayList<>();
    ArrayList<value> visited = new ArrayList<>();

    double grad=0.0;
    double data=0.0;
    String label="";
    String op="";

    value(double data,String label){
        this.data=data;
        this.label=label;
    }
    @Override
    public String toString(){
        return "Value(DATA)="+data;
    }
    value add(value v){
        value out= new value(this.data+v.data,"add");
    }

}
class Nn{
    public static void main(String[] args){
        value v1=new value(1.0,"v1");
        System.out.println(v1);
    }
}
