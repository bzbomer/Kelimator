package Grup19Kelimator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Yönetici {

    private JFrame frame;
    private JPanel GUI;
    private DefaultListModel<String> listModel;
    private JList<String> jList;
    private JButton ekle , cikar , kapat , geriAl;
    private ArrayList<String> eklenecekler =new ArrayList<>();
    private Stack<String> cikarilanlar;

    Yönetici(){
        guiOlustur();
        kelimeleriEkle();
        cikarilanlar = new Stack<>();

        ekle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String secilen = jList.getSelectedValue();

                    String[] data;
                    ArrayList<String> kelimeler = new ArrayList<>();
                    //data[0]   filename
                    //data[1]   kelime --> anlami  (formati)
                    data = secilen.split(":");
                    String fileName = data[0] + ".csv";
                    File file = new File(fileName);
                    try {

                        Scanner scan = new Scanner(file);
                        while (scan.hasNext()) {
                            kelimeler.add(scan.next());
                        }
                        scan.close();
                    } catch (FileNotFoundException exp) {

                    }
                    try {
                        FileWriter writer = new FileWriter(file, true);
                        if (!kelimeler.contains(data[1])) {
                            writer.write(data[1] + "\n");
                        }
                        writer.close();
                    } catch (FileNotFoundException hata) {
                        hata.printStackTrace();
                    } catch (IOException hata) {
                        hata.printStackTrace();
                    }
                    listModel.remove(selectedIndex);
                    save();
                }
            }
        });

        cikar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String str = jList.getSelectedValue();
                    cikarilanlar.push(str);
                    listModel.remove(selectedIndex);
                    save();
                }
            }
        });

        geriAl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!cikarilanlar.empty()) {
                    String str = cikarilanlar.pop();
                    listModel.addElement(str.toLowerCase());
                    jList.setModel(listModel);
                    save();
                }
            }
        });
        kapat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String [] s){

        boolean sifreDogru = false;

        while(!sifreDogru){
            //String sifre = isimAl("Sifre Giriniz:");
            String sifre = parolaAl();
            if (sifre ==null)
                System.exit(0);
            else {
                if (sifre.equals("123456")){
                    Yönetici yönetici = new Yönetici();
                    break;
                }else{
                    JFrame frame = new JFrame("HATA");
                    JOptionPane.showMessageDialog(frame, "HATALI ŞİFRE");
                }
            }
        }
    }

    private static String parolaAl(){
        JPasswordField parola = new JPasswordField();
        JFrame frame = new JFrame("ŞİFRE EKRANI");
        int returnVal =JOptionPane.showConfirmDialog( frame, parola, "YÖNETİCİ PAROLA", JOptionPane.OK_CANCEL_OPTION );


        if ( returnVal == JOptionPane.OK_OPTION )
        {
            return new String( parola.getPassword( ) );
        }
        else
        {
            return null;
        }
    }

    private void kelimeleriEkle(){
        String fileName = "onerilenler.csv";
        File file = new File(fileName);
        try{
            Scanner inputStream = new Scanner(file);
            String value;
            while (inputStream.hasNext()) {
                value=inputStream.nextLine().toLowerCase();
                listModel.addElement(value);
            }
            jList.setModel(listModel);
            inputStream.close();
        }catch (FileNotFoundException e){
            listModel.addElement("ONERILENlER.CSV BULUNAMADI");
            jList.setModel(listModel);
        }
    }
    private void save(){

        String fileName = "onerilenler.csv";
        File file = new File(fileName);
        try{
            FileWriter writer =new FileWriter(file,false);
            for(int i = 0; i< jList.getModel().getSize();i++){
                writer.write(jList.getModel().getElementAt(i));
            }
            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guiOlustur(){
        frame = new JFrame("Onerilen Kelime Kontrolu");
        frame.setSize(500,350);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GUI = new JPanel();
        GUI.setSize(500,350);
        GUI.setBackground(Color.GRAY);
        GUI.setLayout(null);


        listModel = new DefaultListModel<>();
        jList = new JList<String>();
        TitledBorder title;
        title = BorderFactory.createTitledBorder("Onerilen Kelimeler");
        title.setTitleJustification(TitledBorder.CENTER);
        jList.setBorder(title);
        jList.setSize(320,290);
        jList.setLocation(10,10);
        jList.setVisible(true);
        GUI.add(jList);


        ekle = new JButton();
        ekle.setText("EKLE");
        ekle.setSize(100,30);
        ekle.setLocation(350,50);
        ekle.setVisible(true);
        GUI.add(ekle);

        cikar = new JButton();
        cikar.setText("ÇIKAR");
        cikar.setSize(100,30);
        cikar.setLocation(350,100);
        cikar.setVisible(true);
        GUI.add(cikar);

        geriAl = new JButton();
        geriAl.setText("GERİ AL");
        geriAl.setSize(100,30);
        geriAl.setLocation(350,150);
        geriAl.setVisible(true);
        GUI.add(geriAl);

        kapat = new JButton();
        kapat.setText("KAPAT");
        kapat.setSize(100,30);
        kapat.setLocation(350,270);
        kapat.setVisible(true);
        GUI.add(kapat);

        frame.add(GUI);
    }

}
