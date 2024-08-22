import javax.swing.JFrame;
import java.awt.BorderLayout;

public class NQueens extends JFrame{
   private ControlPanel control;
   private BoardPanel board;

   //constructor parathyrou
   public NQueens() {
      super("NQueens Genetic Algorithm v1.0");
      setLayout(new BorderLayout());
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setResizable(false);
      
      board = new BoardPanel();
      control = new ControlPanel(board);
      
      add(control, BorderLayout.EAST);
      add(board, BorderLayout.WEST);
      
      //sizes the frame so that all its contents are at or above their preferred sizes
      pack();

      // Centers the window and sets it to be visible
      setLocationRelativeTo(null);
      setVisible(true);
   }
   
   public void start() {
      while (true) {
         control.update();  
         // An error can occur if the user clicks restart too fast
         try {
            board.update();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   public static void main(String args[]) {
      //create the window
      NQueens window = new NQueens();
      window.start();
   }
}