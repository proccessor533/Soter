package Soter;
import java.util.Random;
import java.util.Arrays;
import java.io.*;

public class SOTER {
    public static void main(String[] args) {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            int n = 4, m=(2*n-1);

            boolean igrok[][] = new boolean[m][], death = false, WIN = false;

            for ( int i = 0; i < m; i++ ) // Создаем поле игрока
            if (i<n)
                igrok[i]=new boolean[n+i];
            else
                igrok[i]=new boolean[n+(m-i-1)];

            boolean[][] bees = beesPole(igrok);

            int[][] pole = gamePole(bees);

            while(!death && !WIN) {
                System.out.println("\f");
                grafik(igrok, pole);  // отрисовать поле
                System.out.println("\t Введите значение строки и клетки через ентер: ");
                int x = Integer.parseInt(reader.readLine())-1;  	// Integer parseInt -1
                int y = Integer.parseInt(reader.readLine())-1;
                if ( igrok[x][y] ) {
                    System.out.print("Уже открыто");  //как реализовать?
                    Thread.sleep(5000);  //отловить ошибки? проверить задержку
                    continue;
                }
                else if ( bees[x][y] ){
                    death = true;
                }
                else if ( pole[x][y] == 0 ){
                    igrok = PUSTO(igrok, pole, x, y);  //метод заполнения ближайших нулевых клеток трушками с рекурсией
                }
                else
                    igrok[x][y]=true;
                if ( win( igrok, bees ) ) WIN=true;  //вызов метода win
            }
            if (death){
                System.out.println("\f");
                System.out.println("Вы проиграли"); //поржение
                //pokazat' pchol
            }
            else {
                System.out.println("\f");
                System.out.println("Вы победили"); //победа
            }
// NEXT TRY ? ? ?
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean[][] beesPole(boolean[][] pIgrok){  // создание маски с пчелами

        boolean beesP[][] = new boolean[pIgrok.length][];
        for(int i=0; i < pIgrok.length; i++)
            beesP[i] = new boolean[pIgrok[i].length];

        Random r = new Random();
        int count=0, S=0, i, j;

        for ( boolean[] item : beesP )
            for ( boolean it : item ) S++;

        int k = S/5; //определить количество бомб исходя из кол-ва ячеек деленое на 5

        while( count != k ){
            i = r.nextInt(beesP.length); // заменить индексы
            j = r.nextInt(beesP[i].length);
            if (beesP[i][j] != true) {
                beesP[i][j] = true;
                count++;
            }
        }

        return beesP;
    }

    public static int[][] gamePole(boolean[][] pBees) {

        int[][] gPole = new int[pBees.length][];

        for (int i = 0; i < pBees.length; i++) {    //инициализация массива поля игры
            gPole[i] = new int[pBees[i].length];
        }

        for (int i = 0; i < gPole.length; i++) {
            for (int j = 0; j < gPole[i].length; j++) {
                if ( pBees[i][j] )
                    gPole[i][j] = 6;  //Если есть пчелы то максимум
                else
                    gPole[i][j] = 0;  //Если нет пчел то обнуляем
                for (int m = -1; m <= 1; m++)
                    for (int n = -1; n <= 1; n++)
                        if (i + m < 0 || i + m > gPole.length - 1 || j + n < 0 || j + n > gPole[i + m].length - 1 ||
                                (i < gPole.length/2)&&(m==-n) || (i > gPole.length/2)&&(m==n) || (i == gPole.length/2)&&( ((m!=0)&&(n==1)) || ((n==0)&&(m==0)) ) )
                            continue;
                        else if (pBees[i + m][j + n])
                            gPole[i][j] += 1;  //если в рассматриваемой клетке около выбранной есть пчела то прибавляем единичку
            }
        }
        return gPole;
    }

    public static boolean[][] PUSTO(boolean[][] igrokP, int[][] poleG, int x, int y){
        igrokP[x][y]=true;
        for( int m=-1; m<=1; m++)
            for( int n=-1; n<=1; n++)
                if( x+m<0 || x+m>igrokP.length-1 || y+n<0 || y+n>igrokP[x+m].length-1 || m==-n || igrokP[x+m][y+n] )
                    continue;
                else if ( poleG[x+m][y+n]==0 )
                    igrokP = PUSTO(igrokP, poleG, x+m, y+n);
                return igrokP;
    }
    public static boolean win(boolean[][] p, boolean[][] b){

        boolean flag = p[0][0] || b[0][0];

        for( int i=0; i<p.length; i++ )
            for( int j=0; j<p[i].length-1; j++ )
                if (!flag)
                    break;
                else
                    flag = flag && (p[i][j+1] || b[i][j+1]);
                return flag;
        }

    public static void grafik(boolean[][] Igrok, int[][] Pole){

        int k=0;

        for ( int i = 1; i<=(2*Pole.length+1); i++)
            if( i <= 2*Pole[0].length )
                if ( i%2!=0 ){
                    for( int j = 1; j <= Pole[i-k-1].length-i; j++) //itoe??
                        System.out.print(" ");
                    for( int j = 1; j <= Pole[i-k-1].length; j++)
                        System.out.print("/\\");
                    System.out.println();
                }
            else{
                for( int j = 1; j <= Pole[i-k-1].length-i; j++)
                    System.out.print(" ");
                System.out.print("|");
                for( int j = 0; j < Pole[i/2-1].length; j++){
                    System.out.print( (Igrok[(i/2-1)][j]) ? Pole[(i/2-1)][j] : " " );
                    System.out.print("|");
                }
                System.out.println();
                k++;
            }
            else if ( i == (2*Pole[0].length + 1) ){
                for( int j = 1; j <= Pole[i/2-1].length; j++)
                    System.out.print("\\/");
                System.out.println();
                k=1;
            }
            else if ( i > 2*Pole[0].length+1 )
                if ( i%2==0 ){
                    for( int j = 1; j <= k; j++)
                        System.out.print(" ");
                    k++;
                    System.out.print("|");
                    for( int j = 0; j < Pole[i/2-1].length; j++){
                        System.out.print( (Igrok[(i/2-1)][j]) ? Pole[(i/2-1)][j] : " " );
                        System.out.print("|");
                    }
                    System.out.println();
                }
                else{
                    for( int j = 1; j <= k; j++)
                        System.out.print(" ");
                    for( int j = 1; j <= Pole[i/2-1].length; j++)
                        System.out.print("\\/");
                    System.out.println();
                }
    }
}