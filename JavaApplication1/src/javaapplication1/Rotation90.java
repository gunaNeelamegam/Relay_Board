package javaapplication1;


public class Rotation90 {
  /*char star[5][5] =  {
                    {' ',' ','*',' ',' '},
                    {' ','*','*','*',' '},
		    {' ','*','*'.'*',' '},
		    {' ','*','*','*',' '},
		    {' ',' ','*',' ',' '}
		  }
*/
    
public static void main(String[] args) {

    int spc=5;
    int str=0;
     int num=5;
    for(int i=0;i<num;i++)
    {
    for(int j=0;j<spc;j++)
    {
        System.out.print("  ");
    }
    for(int k=0;k<str;k++)
    {
        System.out.print("* ");
    }
if(i==0)
{
    str=str+3;
    spc=spc-4;
}  
if(i==1)
{
    str=str+2;
    spc--;
}
if(i==num/2)
{
    spc++;
    str=str-2;
}
if(i==3)
{
    str=str-3;
     spc=spc+4;
}
        System.out.println("");

}

}
}

