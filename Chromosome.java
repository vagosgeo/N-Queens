public class Chromosome {
   private int[] positions;
   
   // How many queens are attacking eachother
   private int fitness;
   
   public Chromosome(int[] positions) {
      this.positions = positions;
      fitness = calculateFitness();
   }
   
   public int[] getPositions() {
      return positions;
   }
   
   public int getFitness() {
      return fitness;
   }
   
   public int calculateFitness() {

      int Threats = 0;
        for(int i = 0; i < this.positions.length; i++)
        {
            for(int j = i+1; j < this.positions.length; j++)
            {
               //idia grammh
               if(this.positions[i] == this.positions[j]) 
               Threats++;
               //idia diagwnios    
               if(Math.abs(i - j) == Math.abs(this.positions[i] - this.positions[j]))
                  Threats++;
            }
        }
        return Threats;

   }

}