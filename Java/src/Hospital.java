import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Hospital
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 20:58
 */
public class Hospital {


    private Point point = new Point(850, 300);

    private int width;
    private int height = 310;

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }

    private static Hospital hospital = new Hospital();
    public static Hospital getInstance(){
        return hospital;
    }

    private List<Bed> beds = new ArrayList<>();

    private Hospital() {
        if(Constants.BED_COUNT==0){
            width=0;
            height=0;
        }
        int column = Constants.BED_COUNT/50;
        width = column*6+10;

        for(int i=0;i<column;i++){

            for(int j=0;j<50;j++){
                Bed bed = new Bed(point.getX()+i*6+5, point.getY()+j*6+5);
                beds.add(bed);

            }

        }
    }

    public Bed pickBed(){
        for(Bed bed:beds){
            if(bed.isEmpty()){
                return bed;
            }
        }
        return null;
    }

    public Bed returnBed(int x, int y) {
        for (Bed bed:beds) {
            if (!bed.isEmpty() && bed.getX() == x && bed.getY() == y) {
                return bed;
            }
        }
        return null;
    }
}
