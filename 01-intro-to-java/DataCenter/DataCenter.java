public class DataCenter {
    public static int getCommunicatingServersCount(int[][] map){
        int result = 0;
        if( map == null ) return 0;
        final int ROWS = map.length;
        final int R_LENGTH = map[0].length;

        int[] rols_arr = new int[ROWS];
        int[] cols_arr = new int[R_LENGTH];

        for(int i = 0; i < ROWS; i++){

            for(int j = 0 ; j < R_LENGTH; j++){
                if(map[i][j] == 1){
                    rols_arr[i]++;
                    cols_arr[j]++;
                }
            }
        }

        for(int i = 0 ; i < ROWS; ++i){
            for(int j = 0 ; j < R_LENGTH; ++j){
                if(map[i][j] == 1 && (rols_arr[i] != 1 || cols_arr[j] != 1)) result++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 0}, {0, 1}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 0}, {1, 1}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1},{0,0,0,0}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 0, 1},{1, 1, 1},{0, 0, 0}}));
        System.out.println(getCommunicatingServersCount(new int[][]{{1, 1, 1},{1, 1, 0},{0, 0, 1}}));

    }
}
