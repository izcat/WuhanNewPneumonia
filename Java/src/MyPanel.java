import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: Bruce Young
 * @date: 2020年02月02日 17:03
 */
public class MyPanel extends JPanel implements Runnable {

    private int pIndex=0;
    private JLabel timelabel = new JLabel("第0天");
    private JLabel infolabel = new JLabel();
    public static int worldTime = 0;

    private int[] infectedTotal = new int[1000];   // 累计感染的总人数
    private int[] confirmedTotal = new int[1000];  // 累计确诊总人数
    private int[] curedTotal = new int [1000];     // 累计治愈总人数
    private int index = 0;

    private JButton button1 = new JButton("全员佩戴口罩!");
    private JButton button2 = new JButton("全市交通管制!");
    private JButton button3 = new JButton("恢复全市交通!");

    public MyPanel() {
        this.setLayout(null);
        timelabel.setBounds(850, 630, 300, 20);
        timelabel.setFont(new Font("宋体", 0, 18));
        timelabel.setForeground(Color.RED);
        infolabel.setBounds(850, 660, 400, 20);
        infolabel.setFont(new Font("宋体", 0, 18));
        infolabel.setForeground(Color.RED);

        button1.setBounds(850, 700, 150, 30);
        button2.setBounds(850, 750, 150, 30);
        button3.setBounds(1000, 750, 150, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.BROAD_RATE = 0.1f;
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.u = -0.99f;
                Constants.CITY_BLOCK_TIME = worldTime*10;
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.BROAD_RATE = 0.8f;
                Constants.u = 0.99f;
                Constants.CITY_BLOCK_TIME = -1;
            }
        });

        this.add(timelabel);
        this.add(infolabel);
        this.add(button1);
        this.add(button2);
        this.add(button3);
        this.setBackground(new Color(0x444444));
    }

    @Override
    public void paint(Graphics arg0) {
        super.paint(arg0);
        //draw border
        arg0.setColor(new Color(0x00ff00));
        arg0.drawRect(Hospital.getInstance().getX(),Hospital.getInstance().getY(),
                Hospital.getInstance().getWidth(),Hospital.getInstance().getHeight());


        List<Person> people = PersonPool.getInstance().getPersonList();
        if(people==null){
            return;
        }
        people.get(pIndex).update();
        for(Person person:people){

            switch (person.getState()){
                case Person.State.NORMAL:{
                    arg0.setColor(new Color(0xdddddd));

                }break;
                case Person.State.SHADOW:{
                    arg0.setColor(new Color(0xffee00));
                }break;
                case Person.State.CONFIRMED:
                case Person.State.FREEZE:{
                    arg0.setColor(new Color(0xff0000));
                }break;
                case Person.State.CURED:{
                    arg0.setColor(new Color(0x00ff00));
                }break;
            }
            person.update();
            arg0.fillOval(person.getX(), person.getY(), 3, 3);

        }
        pIndex++;
        if(pIndex>=people.size()){
            pIndex=0;
        }
    }



    @Override
    public void run() {
        while (true) {

            this.repaint();

            try {
                Thread.sleep(100);
                worldTime++;
                timelabel.setText("第"+(worldTime/10)+"天");

                if(worldTime>=Constants.CITY_BLOCK_TIME) {
                    timelabel.setText("第"+(worldTime/10)+"天, 已启动交通管制！");
                }

                int infectedNum = PersonPool.getInstance().getInfectedNum();
                int confirmedNum = PersonPool.getInstance().getConfirmedNum();
                int curedNum = PersonPool.getInstance().getCuredNum();
                infolabel.setText("累计感染"+infectedNum+"人,已确诊"+confirmedNum+"人,已治愈"+curedNum+"人");

                if(index==worldTime/10) {
                    infectedTotal[index] = infectedNum;
                    confirmedTotal[index] = confirmedNum;
                    curedTotal[index] = curedNum;
                    index++;
                }

                // days[index] = worldTime / 10;
                // infectedTotal[index] = infectedNum;
                // confirmedTotal[index] = confirmedNum;
                // curedTotal[index] = curedNum;

                // if (index == 0 || days[index] > days[index-1]) {
                //     System.out.print(days[index]);
                //     System.out.print(' ');
                //     System.out.print(infectedTotal[index]);
                //     System.out.print(' ');
                //     System.out.print(confirmedTotal[index]);
                //     System.out.print(' ');
                //     System.out.println(curedTotal[index]);
                // }
                // index++;
            } catch (InterruptedException e) {
                System.out.println(1);
                e.printStackTrace();
            }
        }
    }

    public void saveData(String filePath){
        try {
            // File fout = new File("data.csv");
            File fout = new File(filePath);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            
            // BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(""));
            // 表头
            String head = "Days,Infected,Confirmed,Cured";
            out.write(head);
            out.newLine();  

            for(int i=0;i<index;i++) {
                String line = ""+(i+1)+","+infectedTotal[i]+"," + confirmedTotal[i]+","+curedTotal[i];
                out.write(line);
                out.newLine();
            }

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
        
}
