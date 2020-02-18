import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: PersonPool
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:21
 */
public class PersonPool {
    private static PersonPool personPool = new PersonPool();
    public static PersonPool getInstance(){
        return personPool;
    }

    List<Person> personList = new ArrayList<Person>();

    private static int confirmedNum = 0;
    private static int infectedNum = Constants.ORIGINAL_COUNT;
    private static int curedNum = 0;

    public List<Person> getPersonList() {
        return personList;
    }

    public static void addInfectedNum() {
        infectedNum++;
    }

    public int getInfectedNum() {
        return infectedNum;
    }

    public static void addConfirmedNum() {
        confirmedNum++;
    }

    public int getConfirmedNum() {
        return confirmedNum;
    }

    public static void addCuredNum() {
        curedNum++;
    }

    public int getCuredNum() {
        return curedNum;
    }

    private PersonPool() {
        City city = new City(400,400);
        for (int i = 0; i < 10000; i++) {
            Random random = new Random();
            int x = (int) (100 * random.nextGaussian() + city.getCenterX());
            int y = (int) (100 * random.nextGaussian() + city.getCenterY());
            if(x>700){
                x=700;
            }
            Person person = new Person(city,x,y);
            personList.add(person);
        }
    }
}

