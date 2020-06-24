package point;

import java.util.ArrayList;
import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        ArrayList<Point> points = new ArrayList<>();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                points.add(new Point(i,j));
            }
        }
        points.add(null);

        PolyLine line = new PolyLine(points);

        System.out.println(line.toString());
    }
}
