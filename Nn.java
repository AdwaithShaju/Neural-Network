import java.util.*;
import java.lang.Math;
import java.text.DecimalFormat;

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
        DecimalFormat df = new DecimalFormat("##.####"); 
        return "" + df.format(data);
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
class Neuron{
    ArrayList<value> w = new ArrayList<>();
    Random random = new Random();
    value b;

    Neuron(int n){
        for(int i=0;i<n;i++){
            double wts = (random.nextDouble()*2)-1;
            this.w.add(new value(wts));
        }
        double bias = (random.nextDouble()*2)-1;
        this.b = new value(bias);
    }

    value  call(ArrayList<value> x){
        value wixi,out;
        value act = new value(0.0);
        for(int i =0; i<this.w.size();i++){
            wixi = w.get(i).mul(x.get(i));
            act = act.add(wixi);
        }
        act = act.add(this.b);
        out = act.tanh();
        return out;   
    }

    ArrayList<value> parameters(){
        ArrayList<value> p =new ArrayList<>();
        p.addAll(this.w);
        p.add(this.b);
        return p;
    }    
}

class Layer{
    ArrayList<Neuron> neurons = new ArrayList<>();
    Layer(int nin,int nout){
        for (int i = 0; i < nout; i++) {
            this.neurons.add(new Neuron(nin));
        }
    }

    ArrayList<value> call(ArrayList<value> x){
        ArrayList<value> outs = new ArrayList<>();
        for (Neuron neuron : this.neurons) {
            outs.add(neuron.call(x));
        }
        return outs;
    } 

    ArrayList<value> parameters(){
        ArrayList<value>par= new ArrayList<>();
        for (Neuron neuron : this.neurons) {
            par.addAll(neuron.parameters());
        }
        return par;
    }
}
class MLP{
    ArrayList<Layer> layers = new ArrayList<>();
    MLP(int nin,int[] nouts){
        for (int i = 0; i < nouts.length; i++) {
            this.layers.add(new Layer(nin, nouts[i]));
            nin=nouts[i];
        }
    }
    ArrayList<value> call(ArrayList<value> x){
        for (Layer layer : this.layers) {
                x=layer.call(x);       
        }
        return x;
    }
    ArrayList<value> parameters(){
        ArrayList<value>params= new ArrayList<>();
        for (Layer layer : this.layers) {
            params.addAll(layer.parameters());
        }
        return params;
    }
    
}
class Nn{
    public static void main(String[] args){

        ArrayList<ArrayList<value>> y = new ArrayList<>();
        ArrayList<ArrayList<value>> xall = new ArrayList<>();
        ArrayList<value> ypred = new ArrayList<>();
        ArrayList<value> yact = new ArrayList<>();

        value loss = new value(0);

        yact.add(new value(1));
        yact.add(new value(-1));
        yact.add(new value(-1));
        yact.add(new value(1));

        ArrayList<value> x =new ArrayList<>();

        x.add(new value(2.0));
        x.add(new value(3.0));
        x.add(new value(-1));
        xall.add(x);
        x = new ArrayList<>();
        x.add(new value(3.0));
        x.add(new value(-1.0));
        x.add(new value(0.5));
        xall.add(x);
        x = new ArrayList<>();
        x.add(new value(0.5));
        x.add(new value(1.0));
        x.add(new value(1));
        xall.add(x);
        x = new ArrayList<>();
        x.add(new value(1.0));
        x.add(new value(1.0));
        x.add(new value(-1));
        xall.add(x);

        int[] nouts = {4,4,1};
        MLP m = new MLP(3,nouts);
        ArrayList<value> params;
        int resume = 200;
        int cnt =1;
        do{
            for (ArrayList<value> xi : xall) {
                y.add(m.call(xi));
            }
            for (ArrayList<value> yi : y) {
                        ypred.add(yi.get(0));
            }
            System.out.println("Epoch  "+cnt);
            cnt++;
            System.out.println("    Predicted = "+ypred);
            System.out.println("    Actual = "+yact);
            
            for (int i = 0; i < ypred.size(); i++) {
                loss=loss.add((ypred.get(i).sub(yact.get(i)).pow(2)));
            }
            loss.grad=1;
            loss.reverse();
            System.out.println("    Loss = "+loss);
            params=m.parameters();
            for (value v : params) {
                v.data+= -0.1*v.grad;
            }
            for (value v : params) {
                v.grad=0.0;
            }
            ypred = new ArrayList<>();
            y = new ArrayList<>();
            loss=new value(0);
            resume--;
        }while(resume>0);

    }
}
