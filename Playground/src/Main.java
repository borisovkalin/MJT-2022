public class Main {
    //01-stock-exchange

    public static int maxProfit(int[] prices){
        if(prices == null) return 0;
        //sliding window style tactic
        int index_start = 0;
        int index_end = 0;
        int winnings=0;
        final int ARRAY_SIZE = prices.length;
        //{7, 1, 5, 3, 6, 4}
        boolean calculate = false;

        for(int i = 0 ;i < ARRAY_SIZE; ++i){

            if(prices[index_end] > prices[i] && index_end != 0){
                calculate = false;
                winnings += prices[index_end] - prices[index_start];
                index_start = i;
                index_end = i;
            }
            if(prices[index_start] > prices[i]) {
                index_start = i;
            }

            if(prices[index_start] < prices[i]) {
                index_end = i;
                calculate = true;
            }

        }

        if(calculate){
            winnings += prices[index_end] - prices[index_start];
        }
        return winnings;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        System.out.println(maxProfit(new int[]{1, 2, 3, 4, 5}));
        System.out.println(maxProfit(new int[]{7, 6, 5, 4, 3, 2, 1}));
    }
}