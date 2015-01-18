/**
 * Created by alvaro on 18/01/15.
 */
public class Pascal {


    private static int p( int x, int y ) {

        if (x == 1 && y == 1) {
            return 1;
        } else if (x == -1) {
            return 0;
        } else if (x > y) {
            return 0;
        } else {
            return p(x - 1, y - 1) + p(x, y - 1);
        }
    }



    private static void imprimeTriangulo( int filas ){
        for( int y = 1 ; y <= filas ; y += 1 ){
            for( int x = 1 ; x <= y ; x += 1 ){
                System.out.printf( "%10d", p(x,y) );
            }
            System.out.println();
        }
    }

    public static void main( String[] args ){
        imprimeTriangulo(20);
    }
}
