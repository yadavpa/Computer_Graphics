//end point code algorithm
//P1 and P2 are the end points of line
package org.yourorghere;

import java.util.Arrays;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class OutcodeAlgorithm {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        final GLCanvas canvas=new GLCanvas(cap);
        RendAlgo r=new RendAlgo(); 
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("Scanline SeedFill Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}

class RendAlgo implements GLEventListener{
    private int[] computeCode(double x,double y,double xl,double xr,double yt,double yb){
        //xl,xr,yt,yb are left,right,top and bottom window coordinates
        int code[]=new int[4];
        if(x < xl) code[3]=1;
        else       code[3]=0;
        if(x > xr) code[2]=1;
        else       code[2]=0;
        if(y < yb) code[1]=1;
        else       code[1]=0;
        if(y > yt) code[0]=1;
        else       code[0]=0;
        return code;
    }
    private int[] bitwise_and(int a[],int b[]){
        int c[]=new int[a.length];
        for(int i=0;i<a.length;i++){
            c[i]= a[i]&b[i];
        }
        return c;
    }
    //Implementing Cohen Sutherland algorithm
    //Clipping a line from P1 = (x1,y1)  to P2 (x2,y2)
    private double[] cohenSutherlandClip(GL gl,double x1, double y1,double x2,double y2,double xl,double xr,double yt,double yb){
        int p1code[]=new int[4];
        int p2code[]=new int[4];
        double res[]=new double[4];
        boolean accept=false;
        p1code= computeCode(x1,y1,xl,xr,yt,yb);
        p2code= computeCode(x2,y2,xl,xr,yt,yb);
        for(int i=0;i<4;i++){
            System.out.println(p1code[i]+" "+p2code[i]);
        }
        int inside[]={0,0,0,0};
        while(true){
            if(Arrays.equals(p1code,inside) && Arrays.equals(p2code,inside)){
                accept=true; //line is completely inside
                break;
            }
            else if(!Arrays.equals(bitwise_and(p1code,p2code),inside)){
                break; //line is completely invisible/outside
            }
            else{
                int[] code_out=new int[4];
                double x=0,y=0;
                double m= (y2-y1)/(x2-x1);
                if(!Arrays.equals(p1code,inside))
                    code_out= p1code;
                else
                    code_out= p2code;
                //Find intersection points
                if(code_out[3]==1){ //point is to the left of the clip rectangle
                    y= m*(xl - x1)+y1;
                    x= xl;
                }
                if(code_out[2]==1){
                    y= m*(xr - x1)+y1;
                    x= xr;
                }
                if(code_out[1]==1){
                    x= (1/m)*(yb - y1) + x1;
                    y=yb;
                }
                if(code_out[0]==1){
                    x= (1/m)*(yt - y1) + x1;
                    y=yt;
                }
               //Replace point outside the rectangle with intersection point
                if(code_out== p1code){
                    x1=x;
                    y1=y;
                    p1code=computeCode(x1,y1,xl,xr,yt,yb);               
                }
                else{
                    x2=x;
                    y2=y;
                    p2code=computeCode(x2,y2,xl,xr,yt,yb);
                }   
            }
        }
        if(accept){
            System.out.println("Line accepted from ("+x1+","+y1+") to ("+x2+","+y2+")");
            res[0]=x1;res[1]=y1;res[2]=x2;res[3]=y2;
        }
        else{
            System.out.println("Line rejected");
        }
       return res;
    }
    
    public void init(GLAutoDrawable drawable) {
        final GL gl=drawable.getGL();
        final GLU glu=new GLU();
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glViewport(0,0,640,480);
	gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(0, 640, 0, 480);
    }

    public void display(GLAutoDrawable drawable) {
        final GL gl=drawable.getGL();
        final GLU glu=new GLU();
        gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
        gl.glColor3f(0.0f,1.0f,1.0f);
        double xl=40,xr=100,yt=80,yb=40;
        lineDDA(gl,xl,yb,xr,yb);
        lineDDA(gl,xr,yb,xr,yt);
        lineDDA(gl,xr,yt,xl,yt);
        lineDDA(gl,xl,yt,xl,yb);
        //lineDDA(gl,50,50,70,70);
        double res[]=cohenSutherlandClip(gl,70,90,110,40,xl,xr,yt,yb);
        lineDDA(gl,res[0],res[1],res[2],res[3]);
    }
    
public void lineDDA(GL gl,double x1,double y1,double x2,double y2) {
        int dx= (int)(x2  -x1);
        int dy= (int)(y2 - y1);
        int steps;
        double x_increment,y_increment,x= x1,y=y1;
        if(Math.abs(dx) > Math.abs(dy))
            steps=Math.abs(dx);
        else
            steps=Math.abs(dy);
        x_increment= (float)dx/(float)steps;
        y_increment= (float)dy/(float)steps;
        for(int i=0;i<steps;i++){
            setpixel(gl,Math.round(x),Math.round(y));
            x= x+ x_increment;
            y= y+ y_increment;
        }    
    }
    private void setpixel(GL gl,double x,double y){
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2d(x, y);
        gl.glEnd( );
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
      
    }
    
}