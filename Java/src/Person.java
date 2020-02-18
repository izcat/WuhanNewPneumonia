import java.util.List;
import java.util.Random;

/**
 * @ClassName: Person
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:05
 */
public class Person {
    private City city;
    private int x;
    private int y;
    private MoveTarget moveTarget;
    int sig=1;
    int hospital_time = -1; //住院的时间
    int infectedTime=0;
    int confirmedTime=0;

    double targetXU;
    double targetYU;
    double targetSig=50;


    public interface State{
        int NORMAL = 0;
        int SUSPECTED = NORMAL+1;
        int SHADOW = SUSPECTED+1;

        int CONFIRMED = SHADOW+1;
        int FREEZE = CONFIRMED+1;
        int CURED = FREEZE+1;
    }

    public Person(City city, int x, int y) {
        this.city = city;
        this.x = x;
        this.y = y;
        targetXU = 100*new Random().nextGaussian()+x;
        targetYU = 100*new Random().nextGaussian()+y;

    }
    public boolean wantMove(){
        double value = sig*new Random().nextGaussian()+Constants.u;
        return value>0;
    }

    private int state=State.NORMAL;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isInfected(){
        return state>=State.SHADOW;
    }
    public void beInfected(){
        state = State.SHADOW;
        infectedTime=MyPanel.worldTime;
    }

    public double distance(Person person){
        return Math.sqrt(Math.pow(x-person.getX(),2)+Math.pow(y-person.getY(),2));
    }

    private void freezy(){
        state = State.FREEZE;
    }
    private void moveTo(int x,int y){
        this.x+=x;
        this.y+=y;
    }
    private void action(){
        if(state==State.FREEZE){
            return;
        }
        if(!wantMove()){
            return;
        }
        if(moveTarget==null||moveTarget.isArrived()){

            double targetX = targetSig*new Random().nextGaussian()+targetXU;
            double targetY = targetSig*new Random().nextGaussian()+targetYU;
            moveTarget = new MoveTarget((int)targetX,(int)targetY);

        }


        double dX = moveTarget.getX()-x;
        double dY = moveTarget.getY()-y;
        double length = Math.sqrt(dX*dX + dY*dY);

        if(length<1){
            moveTarget.setArrived(true);
            return;
        }

        int udX = (int) (1.5*dX/length);
        int udY = (int) (1.5*dY/length);
        // 1.414


        if(x>=800 && udX>0){
            moveTarget=null;
            udX = -udX;
        }

        if(x<=0 && udX<0){
            moveTarget=null;
            udX = -udX;
        }

        if(y>=800 && udY>0){
            moveTarget=null;
            udY = -udY;
        }

        if(y<=0 && udY<0){
            moveTarget=null;
            udY = -udY;
        }

        moveTo(udX,udY);

//        if(wantMove()){
//        }


    }

    private float SAFE_DIST = 0.5f;

    public void update(){
        //@TODO找时间改为状态机
//        if(state>=State.FREEZE){
//            return;
//        }
        // 随机活动
        action();

        // 检查是否被感染
        if(state==State.NORMAL){
            List<Person> people = PersonPool.getInstance().personList;
            for(Person person:people){
                if(person.getState()==State.NORMAL||person.getState()==State.FREEZE){
                    continue;
                }
                float random = new Random().nextFloat();
                if(random<Constants.BROAD_RATE&&distance(person)<SAFE_DIST){
                    this.beInfected();
                    PersonPool.addInfectedNum();
                    break;  // 要加break
                }
            }
        }

        

        if(state==State.SHADOW && MyPanel.worldTime-infectedTime>=Constants.SHADOW_TIME){
            state=State.CONFIRMED;
            PersonPool.addConfirmedNum();
            confirmedTime = MyPanel.worldTime;
        }

        if(state==State.CONFIRMED && MyPanel.worldTime-confirmedTime>=Constants.HOSPITAL_RECEIVE_TIME){
            Bed bed = Hospital.getInstance().pickBed();
            if(bed==null){
                System.out.println("隔离区没有空床位");
            }else{
                state=State.FREEZE;
                x=bed.getX();
                y=bed.getY();
                bed.setEmpty(false);
                if (hospital_time == -1){
                    hospital_time = MyPanel.worldTime;
                }
            }
        }

        if (state==State.FREEZE) {
            if (MyPanel.worldTime - hospital_time > Constants.CURED_TIME) {
                state = State.CURED;
                PersonPool.addCuredNum();
                Bed bed = Hospital.getInstance().returnBed(x, y);
                bed.setEmpty(true);
                x = (int) (100 * new Random().nextGaussian() + city.getCenterX());
                y = (int) (100 * new Random().nextGaussian() + city.getCenterY());
                if (x > 700) {
                    x = 700;
                }
            }
        }

        
    }
}

