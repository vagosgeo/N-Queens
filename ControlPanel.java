import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

public class ControlPanel extends JPanel {
   private long start;
   private long now;
   private long end;
   private boolean ended = false;
   private BoardPanel board;
   private Font font = new Font("Monospaced", Font.BOLD, 24);
   private String[] boardList, mutationList, selectionList, populationList, maxTimeList, pauseList;
   private JComboBox <String[]> boardDropdown, mutationDropdown, selectionDropdown, populationDropdown, maxTimeDropdown, pauseDropdown;
   private JButton run;
   private Image img = Toolkit.getDefaultToolkit().getImage("imgs\\darkBrownWoodControl.jpg");
   public void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.drawImage(img, 0, 0, null);
      }
   public ControlPanel(BoardPanel b) {
      this.board = b;
      setPreferredSize(new Dimension(300, 720));
      
      setLayout(new FlowLayout(FlowLayout.LEFT));

      setBorder(new EmptyBorder(new Insets(150, 50, 150, 25)));
      
      boardList = new String[]{"8", "9", "10", "12","15", "18", "20", "25", "30",
       "35", "45", "60", "120", "150", "180", "240", "360", "720"};
      mutationList = new String[]{"10", "20", "30", "40", "50", "60", "70",  "80", "90", "100"};
      selectionList = new String[]{"2", "3", "4", "5", "10", "15"};
      populationList = new String[]{"25", "50", "75", "100", "150", "200", "300"};
      maxTimeList = new String[]{"1", "2", "3", "4", "5", "10", "20", "30", "60", "Unlimited"};
      pauseList = new String[]{"0", "50", "100", "250", "500", "1000"}; 
       
      boardDropdown = new JComboBox (boardList);
      mutationDropdown = new JComboBox(mutationList);
      selectionDropdown = new JComboBox(selectionList);
      populationDropdown = new JComboBox(populationList);
      maxTimeDropdown = new JComboBox(maxTimeList);
      pauseDropdown = new JComboBox(pauseList);
      
      boardDropdown.setSelectedItem("8");
      mutationDropdown.setSelectedItem("80");
      selectionDropdown.setSelectedItem("5");
      populationDropdown.setSelectedItem("150");
      maxTimeDropdown.setSelectedItem("Unlimited");
      pauseDropdown.setSelectedItem("0");
      start = System.currentTimeMillis();
      
      JLabel sizeLabel = new JLabel("Number of Queens");
      JLabel mutationLabel = new JLabel("Mutation Chance (%)");
      JLabel selectionLabel = new JLabel("Selection Factor ");
      JLabel populationLabel = new JLabel("Population Size");
      JLabel maxTimeLabel = new JLabel("Max time (Secs)");
      JLabel pauseLabel = new JLabel("Delay (Millisecs)");
      
      run = new JButton("Run");
      
      run.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent a) {
            // Update the variables and reset
            board.restart(getBoardSize(), getPopulationSize(), getMutationChance(), getSelectionFactor(), getMaxTime(), getPauseLength(), start = System.currentTimeMillis());
            ended = false;
            repaint();
            
         }
      });
      
      sizeLabel.setForeground(Color.WHITE);
      mutationLabel.setForeground(Color.WHITE);
      selectionLabel.setForeground(Color.WHITE);
      populationLabel.setForeground(Color.WHITE);
      maxTimeLabel.setForeground(Color.WHITE);
      pauseLabel.setForeground(Color.WHITE); 
      
      sizeLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
      mutationLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
      selectionLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
      populationLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
      maxTimeLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
      pauseLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
      
      add(sizeLabel);
      add(boardDropdown);
      add(Box.createRigidArea(new Dimension(0, 40)));
      add(mutationLabel);
      add(mutationDropdown);
      add(Box.createRigidArea(new Dimension(0, 40)));
      add(selectionLabel);
      add(selectionDropdown);
      add(Box.createRigidArea(new Dimension(0, 40)));
      add(populationLabel);
      add(populationDropdown);
      add(Box.createRigidArea(new Dimension(0, 40)));
      add(maxTimeLabel);
      add(maxTimeDropdown);
      add(Box.createRigidArea(new Dimension(0, 40)));
      add(pauseLabel);
      add(pauseDropdown);
      add(Box.createRigidArea(new Dimension(0, 40)));
      add(Box.createHorizontalStrut(60));
      add(run);
      add(Box.createRigidArea(new Dimension(0, 40)));
   }
   
   public void update() {
      repaint();
   }
   
   public void paint(Graphics g) {
      super.paint(g);
      int fitness =  board.getBestChrom().getFitness();
      now = System.currentTimeMillis();
      g.setColor(Color.WHITE);
      g.setFont(font);
      g.drawString("N Queens Genetic", 40, 70);
      g.drawString("Algorithm", 90, 105);
      g.drawString("Fitness: " + fitness, 50, 600);//
      if (fitness != 0 && (now - start)/1000 < getMaxTime())
         g.drawString("Seconds: " + (double) (now - start) / 1000, 50, 650);
      else
      {
         if(!ended)
         {
            end = now-start;
            ended = true;
         }
         g.drawString("Seconds: " + (double) (end) / 1000, 50, 650);
      }
   }
   
   public int getBoardSize() {
      return Integer.parseInt(boardList[boardDropdown.getSelectedIndex()]);
   }
   
   public int getMutationChance() {
      return Integer.parseInt(mutationList[mutationDropdown.getSelectedIndex()]);
   }
   
   public int getSelectionFactor() {
      return Integer.parseInt(selectionList[selectionDropdown.getSelectedIndex()]);
   }
   
   public int getPopulationSize() {
      return Integer.parseInt(populationList[populationDropdown.getSelectedIndex()]);
   }
   
   public int getMaxTime() {
      String choice = maxTimeList[maxTimeDropdown.getSelectedIndex()];
      if (choice.equals("Unlimited")) {
         return Integer.MAX_VALUE;
      }
      return Integer.parseInt(choice);
   }
   
   public int getPauseLength() {
      return Integer.parseInt(pauseList[pauseDropdown.getSelectedIndex()]);
   }
}