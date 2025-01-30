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

    value(double data){
        this.data=data;
    } 

    value(double data,ArrayList<value> children,String op){
        this.data=data;
        this.op=op;
        this.prev=children;

    }
    @Override
    public String toString(){
        return "Value(DATA)="+data;
    }
    value add(value v){
        ArrayList<value> children = new ArrayList<>();
        children.add(this);
        children.add(v);
        value out= new value(this.data + v.data,children,"+");
        return out;
    }
    value sub(value v){
        ArrayList<value> children = new ArrayList<>();
        children.add(this);
        children.add(v);
        value out= new value(this.data - v.data,children,"-");
        return out;
    }
    value mul(value v){
        ArrayList<value> children = new ArrayList<>();
        children.add(this);
        children.add(v);
        value out= new value(this.data * v.data,children,"*");
        return out;
    }

}
class Nn{
    public static void main(String[] args){
        value v1=new value(1.0);
        System.out.println(v1);
    }
}
