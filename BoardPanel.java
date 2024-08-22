import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Toolkit;

public class BoardPanel extends JPanel {
   private int numOfQueens = 8;
   private int populationSize = 150;
   private int maxTime = Integer.MAX_VALUE;
   private long start;
   private int mutationChance = 80;
   // oso megalitero, toso pio pithano na epilegoun goneis me mikrotero fitness 
   private int selectionFactor = 5;
   private Chromosome bestChrom;
   private boolean isFinished = false;
   private int pauseTime = 0;
   private int width = 720;
   private int height = 720;
   private int tileWidth = width / numOfQueens;
   private Chromosome[] population;

   public BoardPanel() {
      start = System.currentTimeMillis();
      createRandomPopulation();
      setPreferredSize(new Dimension(width, height));

      //To kalytero chromosome tou population
      bestChrom = getBestChrom();
      // Paint the first generation
      repaint();
   }
   
   public void paint(Graphics g) {
      super.paint(g);
      
      drawBoard(g);
      drawQueens(g);
   }
   
   public void update() {
      if ((System.currentTimeMillis() - start)/1000 < maxTime && bestChrom.getFitness() > 0) {
          try {
             // Time delay apo kinisi se kinisi
             Thread.sleep(pauseTime);
          } catch (Exception ex) {}
          
         // krataw to population san parent
         Chromosome[] parentPopulation = population.clone();

         //sortarei to population wste oi pio katallhloi goneis na einai sthn arxh tou Array
         Arrays.sort(parentPopulation, new Comparator<Chromosome>() {

            public int compare(Chromosome q1, Chromosome q2) {
               return q1.getFitness() - q2.getFitness();
            }
         });
          
         for (int i = 0; i < populationSize; i += 2) {
         
         // epilogh parents apo to parent population
         Chromosome[] parents = selectParents(parentPopulation);
         //anaparagwgh
         Chromosome[] children = reproduce(parents); 
         
         population[i] = children[0]; 
            
         // an to populationSize einai perittos, sto teleutaio zeugari paragw mono ena paidi
         if (i != populationSize - 1) 
            population[i + 1] = children[1]; 
         }
         bestChrom = getBestChrom();
         repaint();
      }
      else {
         isFinished = true;
         
      }
   }
   
   public void restart(int numOfQueens, int popSize, int mutChance, int factor, int maxT, int pause, long st) {
      mutationChance = mutChance;
      selectionFactor = factor;
      maxTime = maxT;
      pauseTime = pause;
      this.numOfQueens = numOfQueens;
      tileWidth = width / numOfQueens;
      populationSize = popSize;
      createRandomPopulation();
      start = st;
      bestChrom = getBestChrom();
      isFinished = false;
      
      // Paint the first generation
      repaint();
   }
   
   //anaparagwgh
   public Chromosome[] reproduce(Chromosome[] parents) {
      Chromosome[] children = new Chromosome[2];
      
      // Generate a random number between 1 and n-1 for the crossover point
      int crossoverPoint = (int) (Math.random() * (numOfQueens - 1)) + 1;
      int[] firstParentPositions, secondParentPositions, firstChildPositions, secondChildPositions;
      
      firstParentPositions = cutArray(parents[0].getPositions(), 0, crossoverPoint);
      secondParentPositions = cutArray(parents[1].getPositions(), crossoverPoint, numOfQueens);
      firstChildPositions = combineArrays(firstParentPositions, secondParentPositions);      
      
      firstParentPositions = cutArray(parents[0].getPositions(), crossoverPoint, numOfQueens);
      secondParentPositions = cutArray(parents[1].getPositions(), 0, crossoverPoint);
      secondChildPositions = combineArrays(secondParentPositions, firstParentPositions);
      
      children[0] = new Chromosome(mutatePositions(firstChildPositions));
      children[1] = new Chromosome(mutatePositions(secondChildPositions));

      return children;
   }
   
   //mutation
   public int[] mutatePositions(int[] positions) {
      int rand = (int) Math.round(Math.random() * 100);
      
      if (rand <= mutationChance) {
         int randIndex = (int) Math.round(Math.random() * (numOfQueens - 1));
         positions[randIndex] = (int) Math.round(Math.random() * (numOfQueens - 1));
      }
  
      return positions;
   }
   
   public int[] combineArrays(int[] array1, int[] array2) {
      int[] newArray = new int[array1.length + array2.length];
      
      System.arraycopy(array1, 0, newArray, 0, array1.length);
      System.arraycopy(array2, 0, newArray, array1.length, array2.length);
      
      return newArray;
   }
   
   public int[] cutArray(int[] array, int startIndex, int endIndex) {
      int arrayLength = endIndex - startIndex;
      
      int[] newArray = new int[arrayLength];
      
      for (int i = 0; i < newArray.length; i++) {
         newArray[i] = array[startIndex + i];
      }
      
      return newArray;
   }
   
   public Chromosome[] selectParents(Chromosome[] parentPopulation) {
      Chromosome[] parents = new Chromosome[2];

      for (int i = 0; i < parents.length; i++) { 
         //create random variable {0, .. , 1.0}, raise sth selectionFactor dinami
         //oso megaliteri i dinami, toso pio pithano einai na paei se xamhloteres theseis tou Array kai na epileksei pio katallhlous parents
         parents[i] = parentPopulation[(int) (populationSize * Math.pow(Math.random(), selectionFactor))];
      }
      return parents;
   }
   
   public boolean isFinished() {
      return isFinished;
   }
   
   //get lowest Fitness
   public Chromosome getBestChrom() {
      Chromosome bestChrom = population[0];
      int minFitness = population[0].getFitness();

      for (int i = 1; i < populationSize; i++) {
         if (population[i].getFitness() < minFitness) {
            minFitness = population[i].getFitness();
            bestChrom = population[i];
         }
      }
      return bestChrom;
   }
   
   //create population apo random chromosomes
   public void createRandomPopulation() {
      population = new Chromosome[populationSize];
      for (int i = 0; i < populationSize; i++) {
         population[i] = new Chromosome(createRandomPositions());
      }
   }
   
   //create random Positions gia ena Chromosome
   private int[] createRandomPositions() {
      int[] positions = new int[numOfQueens];
      for (int i = 0; i < numOfQueens; i++) {
         positions[i] = (int) Math.floor(Math.random() * numOfQueens);
      }
      return positions;  
   }
   
   //fortwse to eikonidio ths Queen
   public Image loadImage() {
      try {
         return ImageIO.read(this.getClass().getResource("imgs\\BlackQueen.png"));
      } catch(IOException ex) {}
      
      return null;
   }
   
   //Draw
   public void drawQueens(Graphics g) {
      int[] positions = bestChrom.getPositions();
      int margin = tileWidth / 8;
      int queenWidth = tileWidth * 6 / 8;
      Image queen = loadImage();

      for (int i = 0; i < positions.length; i++) {
         g.drawImage(queen, i * tileWidth + margin, positions[i] * tileWidth + margin, queenWidth, queenWidth, null);
      }
   }
   
   public void drawBoard(Graphics g) {
      boolean whiteTile = false;
   
      super.paintComponent(g);
      Image img = Toolkit.getDefaultToolkit().getImage("imgs\\darkBrownWoodTile.jpg");
      Image img1 = Toolkit.getDefaultToolkit().getImage("imgs\\lightBrownWoodTile.png");
      
      // Sthles
      for (int i = 0; i < numOfQueens; i++) {
         //Allazw poio Color zvgrafizetai prwto gia na ftiaxtei h skakiera
         if (numOfQueens % 2 == 0)
            whiteTile = !whiteTile;
         // Grammes
         for (int j = 0; j < numOfQueens; j++) {
            if (whiteTile)
               g.drawImage(img1, j * tileWidth, i * tileWidth, tileWidth, tileWidth, null);
            else
               g.drawImage(img, j * tileWidth, i * tileWidth, tileWidth, tileWidth, null);
            
            whiteTile = !whiteTile;
         }
      }
   }
}