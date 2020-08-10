package entities;

public class Routes {
    public int f_id;
    public int t_id;
    public int cost;

    public Routes(int from_id, int to_id, int cost){
        f_id = from_id;
        t_id = to_id;
        this.cost = cost;
    }

    public int getStartIndex(){
        return f_id;
    }

    public int getEndIndex(){
        return t_id;
    }
}
