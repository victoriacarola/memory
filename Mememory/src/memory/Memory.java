package memory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author victoria
 */
public class Memory {
    private JFrame frame;
    private JPanel panelTitle, panelGrid, panelControl;
    private JButton buttonNew, buttonSolve;

    private buttonGame buttonLastClicked;
    private final Images imagens;
    
    private JLabel labelTitle;
    
    Integer intQtdOpened;
    Integer intCombined;
    ArrayList  listShuffle;

    
    private List<buttonGame> listButtons;
    
    private class buttonGame extends JButton{
        Integer iCod;
        public buttonGame(Integer iCod){
            this.iCod = iCod;
        }
    }
    
    private void enlargeFont(java.awt.Component c, float factor) {
        c.setFont(c.getFont().deriveFont(c.getFont().getSize() * factor));
    }
    
    public Memory(){ 
        imagens = new Images();
        intQtdOpened = 0;
        intCombined = 0;

        listShuffle = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            listShuffle.add(i);
            listShuffle.add(i);
        }
        Collections.shuffle(listShuffle);
    }
    
    private void Solve(Boolean bMostrarCliques){
        if(intQtdOpened == -1) return;
        labelTitle.setText("Number of Clicks: " + 
                (bMostrarCliques? intQtdOpened.toString():"Auto Resolution"));

        intQtdOpened = -1;
        intCombined = 9;
        buttonLastClicked = null;
        
        for(int i = 0; i < listButtons.size();i++){
            buttonGame button = listButtons.get(i);
            button.setIcon(imagens.IconFactory((Integer) listShuffle.get(i)));
            button.iCod = 0;
            listButtons.set(i, button);
        }
        panelGrid.repaint();
    }

    private void NewGame(){
        Collections.shuffle(listShuffle);
        intQtdOpened = 0;
        intCombined = 0;
        labelTitle.setText("Number of Clicks: 0");
        buttonLastClicked = null;
        
        for(int i = 0; i < listButtons.size();i++){
            buttonGame button = listButtons.get(i);
            button.iCod = (Integer) listShuffle.get(i);
            button.setIcon(imagens.IconFactory(-1));
            listButtons.set(i, button);
        }
        panelGrid.repaint();

    }

    public void ShowWindow(){
        frame = new JFrame("Memory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Title
        labelTitle = new JLabel("Spielstand : 0");
        enlargeFont(labelTitle, 2);
        
        panelTitle = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED));
        panelTitle.add(labelTitle);
        frame.add(panelTitle,BorderLayout.NORTH);
        
        // Controls
        panelControl = new JPanel(new FlowLayout(FlowLayout.CENTER
                , 50, 0));
        panelControl.setBorder(new BevelBorder(BevelBorder.RAISED));
        buttonNew = new JButton("New Game");
        enlargeFont(buttonNew, 2);
        panelControl.add(buttonNew);
        buttonSolve = new JButton("Solve");
        enlargeFont(buttonSolve, 2);
        panelControl.add(buttonSolve);
        frame.add(panelControl,BorderLayout.SOUTH);

        buttonNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewGame();
            }
        });

        buttonSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Solve(false);
            }
        });


        // grid principal
        panelGrid = new JPanel(new GridBagLayout());
        panelGrid.setBorder(new BevelBorder(BevelBorder.RAISED));
        // 6 x 4 = 24 
        // 24 have two possibilities of 12
        listButtons = new ArrayList<>();
        buttonLastClicked = null;
        int x = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 3; j++){
                Integer intNumSorteado = (Integer) listShuffle.get(x);
                buttonGame buttonItem = new buttonGame(intNumSorteado);
                
                buttonItem.setIcon(imagens.IconFactory(-1));
                x++;
                

                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.weightx = .5;
                c.weighty = .5;
                c.gridx = i;
                c.gridy = j;
                panelGrid.add(buttonItem, c);

                // z�hlt schon ausgew�hlte buttons
                listButtons.add(buttonItem);
                
                buttonItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(buttonItem.iCod == 0){
                            return;
                        }
                        // rule
                        // if there is a doubleclick on the same card , it won't count
                        if(buttonItem.equals(buttonLastClicked)) return;

                        labelTitle.setText("Number of Clicks: " + ++intQtdOpened);
                        
                        buttonItem.setIcon(imagens.IconFactory(buttonItem.iCod));
                        
                        if(buttonLastClicked == null){
                            buttonLastClicked = buttonItem;
                            return;
                        }

                        
                        if(Objects.equals(buttonItem.iCod, buttonLastClicked.iCod)){

                            buttonItem.setIcon(imagens.IconFactory(0));
                            buttonItem.iCod = 0;

                            buttonLastClicked.setIcon(imagens.IconFactory(0));
                            buttonLastClicked.iCod = 0;

                            buttonLastClicked = null;
                            intCombined++;
                            if(intCombined >= 12){
                                Solve(true);
                            }
                                
                        }else{
                            buttonLastClicked.setIcon(imagens.IconFactory(-1));
                            buttonLastClicked = buttonItem;
                        }
                    }
                });
            }
        }
        frame.add(panelGrid,BorderLayout.CENTER);
        
        
        frame.pack();
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setVisible(true);

    }

   
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Memory mem = new Memory();
                mem.ShowWindow();
                
            }
        });

    }
    
}
