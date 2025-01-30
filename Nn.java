import java.util.*;
import java.io.*;
import java.lang.Math;

class value{

    ArrayList<value> topo = new ArrayList<>();
    ArrayList<value> prev = new ArrayList<>();
    ArrayList<value> visited = new ArrayList<>();

    double grad=0.0;
    double data=0.0;
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

    void backward(){
        switch(op){
            case "+":
                value v1 = this.prev.get(0);
                value v2 = this.prev.get(1);
                v1.grad+=this.grad;
                v2.grad+=this.grad;
                break;
            case "-":
                value v3 = this.prev.get(0);
                value v4 = this.prev.get(1);
                v3.grad+=this.grad;
                v4.grad-=this.grad;
                break;
            case "*":
                value v5 = this.prev.get(0);
                value v6 = this.prev.get(1);
                v5.grad+=this.grad*v6.data;
                v6.grad+=this.grad*v5.data;
                break;
            case "^":
                value v7 = this.prev.get(0);
                value v8 = this.prev.get(1);
                v7.grad+=v8.data*Math.pow(v7.data, v8.data-1)*this.grad;
                break;
            case "tanh":
                value v9 = this.prev.get(0);
                v9.grad+=(1-this.data*this.data)*this.grad;
                break;
            default:
                break;
        }
    }

    void reverse(){
        buildtopo(this);
        for(int i=topo.size()-1;i>=0;i--){
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
