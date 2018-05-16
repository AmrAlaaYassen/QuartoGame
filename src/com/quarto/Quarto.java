package com.quarto;

import org.jpl7.Query;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.omg.PortableInterceptor.INACTIVE;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Quarto extends JDialog
{
    private JPanel contentPane;
    public JButton H1;
    public JButton H3;
    public JButton H2;
    public JButton H4;
    public JButton H5;
    public JButton H6;
    public JButton H7;
    public JButton H8;
    public JButton H9;
    public JButton P1;
    public JButton P2;
    public JButton P3;
    public JButton P4;
    public JButton P6;
    public JButton P7;
    public JButton P5;
    public JButton P8;
    public JTextArea instruction;
    public static int Pos=-1;
    public static int Peace=-1;
    public static ArrayList<Integer>peaces;

    public Quarto()
    {
        String command="consult('AI.pl')";
        Query query=new Query(command);
        System.out.println(query.hasSolution());
        peaces=new ArrayList<>();
        for(int i=1;i<=8;i++)
            peaces.add(i);

        try {
            Writer writer=new FileWriter("Next.txt");
            writer.write("[0,0,0,0,0,0,0,0,0].");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Writer writer=new FileWriter("Peaces.txt");
            writer.write("[1,2,3,4,5,6,7,8].");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        setContentPane(contentPane);
        setModal(true);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        P1.addActionListener(e -> {Peace=1;});
        P2.addActionListener(e -> {Peace=2;});
        P3.addActionListener(e -> {Peace=3;});
        P4.addActionListener(e -> {Peace=4;});
        P5.addActionListener(e -> {Peace=5;});
        P6.addActionListener(e -> {Peace=6;});
        P7.addActionListener(e -> {Peace=7;});
        P8.addActionListener(e -> {Peace=8;});


        H1.addActionListener(e -> {Pos=1;if(Peace!=-1)engine();});
        H2.addActionListener(e -> {Pos=2;if(Peace!=-1)engine();});
        H3.addActionListener(e -> {Pos=3;if(Peace!=-1)engine();});
        H4.addActionListener(e -> {Pos=4;if(Peace!=-1)engine();});
        H5.addActionListener(e -> {Pos=5;if(Peace!=-1)engine();});
        H6.addActionListener(e -> {Pos=6;if(Peace!=-1)engine();});
        H7.addActionListener(e -> {Pos=7;if(Peace!=-1)engine();});
        H8.addActionListener(e -> {Pos=8;if(Peace!=-1)engine();});
        H9.addActionListener(e -> {Pos=9;if(Peace!=-1)engine();});



    }


    private void onCancel() {
        dispose();
    }

    public void updatePeaces(String state)
    {
        ArrayList<Integer>arrayList=new ArrayList<>();
        for(int i=0;i<state.length();i++)
            if(Character.isDigit(state.charAt(i)))
                arrayList.add(state.charAt(i)-'0');
        String peacesList="[";
        for(int i=0;i<arrayList.size();i++)
        {
            if(peaces.contains(arrayList.get(i)))
            {
                int index=peaces.indexOf(arrayList.get(i));
                peaces.remove(index);
            }
        }

        for(int i=0;i<peaces.size();i++)
        {
            if(i==peaces.size()-1)
                peacesList+=peaces.get(i).toString();
            else
            {
                peacesList+=peaces.get(i).toString()+",";
            }
        }
        peacesList+="]";
        System.out.println(peacesList);
        Writer writer = null;
        try {
            writer = new FileWriter("Peaces.txt");
            writer.write(peacesList+".");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String parsing(String state)
    {
        ArrayList<Integer>arrayList=new ArrayList<>();
        for(int i=0;i<state.length();i++)
                if(Character.isDigit(state.charAt(i)))
                    arrayList.add(state.charAt(i)-'0');

        updatePeaces(state);
        for(int i=0;i<arrayList.size();i++)
        {
            if(arrayList.get(i)!=0)
            {
                Image image= null;
                try {
                    image = ImageIO.read(getClass().getResource("img/"+arrayList.get(i).toString()+".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                positionMapper(i+1).setIcon(new ImageIcon(image));
                peaceMapper(arrayList.get(i)).setIcon(null);
                peaceMapper(arrayList.get(i)).setEnabled(false);
            }
        }
        if(state.contains("win"))
        {
            if(state.contains("h"))
                return "Computer Win!!";
            else
                return "You Win!!";
        }
        else if(state.contains("draw"))
        {
            return "draw";
        }
        String board="[";
        for(int i=0;i<arrayList.size();i++)
        {
            if(i==arrayList.size()-1)
               board+=Integer.toString(arrayList.get(i));
            else
            {
                board+=Integer.toString(arrayList.get(i));
                board+=',';
            }
        }
        board+=']';
        return board;
    }


    public JButton peaceMapper(int i)
    {
        switch (i)
        {
            case 1:
                return P1;
            case 2:
                return P2;
            case 3:
                return P3;
            case 4:
                return P4;
            case 5:
                return P5;
            case 6:
                return P6;
            case 7:
                return P7;
            case 8:
                return P8;
        }
        return null;
    }

    public JButton positionMapper(int i)
    {
        switch (i)
        {
            case 1:
                return H1;
            case 2:
                return H2;
            case 3:
                return H3;
            case 4:
                return H4;
            case 5:
                return H5;
            case 6:
                return H6;
            case 7:
                return H7;
            case 8:
                return H8;
            case 9:
                return H9;
        }
        return null;
    }

    public void write()
    {
        Writer writer = null;
        try {
            writer = new FileWriter("Pos.txt");
            writer.write(Integer.toString(Pos)+".");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer = new FileWriter("Choice.txt");
            writer.write(Integer.toString(Peace)+".");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeNext(String Next)
    {
        Writer writer = null;
        try {
            writer = new FileWriter("Next.txt");
            writer.write(Next+".");
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public String read()
    {
        String state= null;
        try {
            state = new String(Files.readAllBytes(Paths.get("Next.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return state;
    }

    public void check(String Next)
    {
        if(Next.equals("Computer Win!!")||Next.equals("You Win!!"))
        {
            Final aFinal=new Final(Next);
            aFinal.setSize(this.getWidth(),this.getHeight());
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - aFinal.getWidth()) / 2;
            final int y = (screenSize.height - aFinal.getHeight()) / 2;
            aFinal.setLocation(x,y);
            aFinal.setVisible(true);
            this.setVisible(false);
            System.out.println(Next);
        }

    }

    public void engine()
    {
        write();
        String human=("humanPlay");
        Query query=new Query(human);
        query.oneSolution();
        String state1=read();
        String result1=parsing(state1);
        writeNext(result1);
        check(result1);
        String command=("computerPlay");
        query=new Query(command);
        query.oneSolution();
        String state2=read();
        String result2=parsing(state2);
        writeNext(result2);
        check(result2);
        Pos=Peace=-1;

    }

    public static void main(String[] args)
    {
        Quarto dialog = new Quarto();
        dialog.pack();
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - dialog.getWidth()) / 2;
        final int y = (screenSize.height - dialog.getHeight()) / 2;
        dialog.setLocation(x,y);
        dialog.setTitle("Quarto Game");
        dialog.setVisible(true);
        System.exit(0);
    }
}
