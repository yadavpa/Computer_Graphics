package org.yourorghere;

import com.jogamp.opengl.GL2;
import com.sun.opengl.util.GLUT;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class BezierCurve {
    public static void main(String[] args) { 
        GLCapabilities cap=new GLCapabilities();
        final GLCanvas canvas=new GLCanvas(cap);
        RendCurve r=new RendCurve(); 
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("Beizer Curve");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}

class RendCurve implements GLEventListener{
    List<Point> cpoints = new ArrayList();
    int num;
    private double p_of_u(int[] points,int n,double u){//n=3
        double sum = 0;
        for(int i=0; i<=n; i++){
            sum = sum + points[i]*blending_function(n,i,u);
        }
       return sum;
    }
    
    private double blending_function(int n,int i,double u){
        return cal_combination(n,i)*Math.pow(u,(double)i)*Math.pow((1-u),(double)(n-i));
    }
    private int cal_combination(int n,int i){
        return factorial(n)/(factorial(n-i)*factorial(i));
    }
    
    private int factorial(int n){
        if(n==0)
            return 1;
        return (n*factorial(n-1));
    }
    public void init(GLAutoDrawable drawable) {
        Scanner sc =new Scanner(System.in);
        final GL gl=drawable.getGL();
        final GLU glu=new GLU();
        GLUT glut=new GLUT();
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glViewport(0,0,640,480);
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(0, 640, 0, 480);
        System.out.println("Enter number of control points");  
        //num = sc.nextInt();
        num = 4;
        //for(int i=0;i<num;i++){}
//            double x = sc.nextDouble();
//            double y = sc.nextDouble();
            cpoints.add(new Point(100,100));
            cpoints.add(new Point(200,300));
            cpoints.add(new Point(400,300));
            cpoints.add(new Point(600,400));
    }

    public void display(GLAutoDrawable drawable) {
        final GL gl=drawable.getGL();
        final GLU glu=new GLU();
        final GLUT glut= new GLUT();
        gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
        gl.glColor3f(0.0f,1.0f,1.0f);
        for(int i=0;i<num;i++){
            setpixel(gl,cpoints.get(i).x,cpoints.get(i).y);
        }
//        for(int i=0;i<num;i++){
//            lineDDA(gl,cpoints.get(i).x,cpoints.get(i).y,cpoints.get((i+1)%(num)).x,cpoints.get((i+1)%(num)).y);
//        }
        int[] xpts = new int[cpoints.size()];
        int[] ypts = new int[cpoints.size()];
        for(int i=0;i<cpoints.size();i++){
           xpts[i]=(int)cpoints.get(i).x;
           ypts[i]=(int)cpoints.get(i).y;
        }
        double x=0,y=0,prevx=xpts[0],prevy=ypts[0];
        //setpixel(gl,cpoints.get(0).x,cpoints.get(0).y);
        for(double i=0;i<=1;i=i+0.00001){
            x = p_of_u(xpts,cpoints.size()-1,i);
            y = p_of_u(ypts,cpoints.size()-1,i);
            System.out.println(x+" "+y);
            drawCurve(gl,prevx,prevy,x,y);
            prevx = x;
            prevy = y;
            
        }//setpixel(gl,cpoints.get(cpoints.size()-1).x,cpoints.get(cpoints.size()-1).y);
        drawCurve(gl,x,y,cpoints.get(cpoints.size()-1).x,cpoints.get(cpoints.size()-1).y);
        
    }
    
    private void drawCurve(GL gl,double prevx,double prevy,double x,double y){
        gl.glColor3f(0.0f,0.0f,1.0f);
        gl.glBegin (GL2.GL_LINES);       
        gl.glVertex2d(prevx,prevy);
        gl.glVertex2d(x,y);
        gl.glEnd();
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
    public void setpixel(GL gl,double x,double y){
        gl.glPointSize(3);
        gl.glColor3f(0.0f,0.0f,1.0f);
        gl.glBegin (GL2.GL_POINTS);
        gl.glVertex2d(x, y);
        gl.glEnd( );
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      
    }

    public void dispose(GLAutoDrawable glad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class Point{
    double x;
    double y;
    Point(double x,double y){
        this.x=x;
        this.y=y;
    }
    
    public String toString(){
        return "("+x+","+y+")";
    }
}


    
   
    

