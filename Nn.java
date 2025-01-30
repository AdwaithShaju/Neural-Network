import java.util.*;
import java.io.*;
import java.lang.Math;
import java.lang.reflect.Array;

class value{

    ArrayList<value> topo = new ArrayList<>();
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
    value tanh(){
        ArrayList<value> children = new ArrayList<>();
        double a =this.data;
        double res = (Math.exp(a*2)-1)/(Math.exp(a*2)+1);
        children.add(this);
        value out =new value(res,children,"tanh");
        return out;
    }
    value pow(double v){
        ArrayList<value> children = new ArrayList<>();
        children.add(this);
        children.add(new value(v));
        value out = new value(Math.pow(this.data,v),children,"^");
        return out;
    }

    void buildtopo(value v){
        if(visited.contains(v)==false){
            visited.add(v);
            Iterator<value> it = v.prev.iterator();
            while (it.hasNext()){
                value child = it.next(); 
                buildtopo(child);
            }
            topo.add(v);
        }
    }

    void reverse(){
        buildtopo(this);
        for(int i=topo.size()-1;,i>-1;i--){
            topo.get(i).backward();
        }

    }
}
class Nn{
    public static void main(String[] args){
        value v1=new value(1.0);
        System.out.println(v1);
    }
}
