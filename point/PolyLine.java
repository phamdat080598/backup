package point;

import java.util.ArrayList;
import java.util.List;

public class PolyLine {
    private List<Point> points = new ArrayList<>();

    public PolyLine() {
    }

    public PolyLine(List<Point> points) {
        this.points = points;
    }

    public void appendPoint(int x,int y){
        points.add(new Point(x,y));
    }

    public void appendPoint(Point point){
        points.add(point);
    }

    @Override
    public String toString() {
        int size = points.size();
        for(int i=0;i<size;i++){
            if(points.get(i)==null){
                points.remove(i);
            }
        }
        return "PolyLine{" +
                "points=" + points +
                '}';
    }

    public int getLength(){
        return this.points.size();
    }
}
