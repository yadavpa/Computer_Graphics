package org.yourorghere;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import javax.swing.JFrame;

public class CyrusBeckAlgorithm{
    public static void main(String[] args) {
        GLProfile p= GLProfile.get(GLProfile.GL2);
        GLCapabilities cap=new GLCapabilities(p);
        final GLCanvas canvas=new GLCanvas(cap);
        RendCyrus r=new RendCyrus();
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("Cyrus Beck Line Clipping Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
        final FPSAnimator animator = new FPSAnimator(canvas,400,true);
	animator.start();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }   
}

class RendCyrus implements GLEventListener{
    //P1 and P2 are end points of a line 
    //The number of edges of clipping region is k
    //ni are the k normal vectors
    //fi are k boundary points one for each edge
    //D is the direction of the line P2-P1
    //wi is the weighting function p1-f
    //tl,tv are lower and upper parameter limits
    MyPoint P1 = new MyPoint(50,150);
    MyPoint P2 = new MyPoint(500,350);
    MyPoint D = new MyPoint();
    int x = 0;
    private MyPoint[] cb(MyPoint P1,MyPoint P2,MyPoint n[],MyPoint f[],int k){
        double tl=0,tu= 1; //assuming entire line is visible
        double t;
        int count =0;
        MyPoint w[]=new MyPoint[k];
        D.x = P2.x - P1.x;
        D.y = P2.y - P1.y;
        for(int i=0;i< k;i++){
            w[i] = new MyPoint((P1.x-f[i].x),(P1.y-f[i].y));
            double ddotn = dotProduct1(D,n,i);
            double wdotn = dotProduct2(w,n,i);
            if(ddotn !=0){ //line is not a point
                t = -wdotn/ddotn;
                if(ddotn > 0){
                    if(t > 1)
                        return null;
                    else
                        tl = Math.max(t, tl);
                }else{
                    if(t < 0)
                        return null;
                    else
                        tu = Math.min(t, tu); 
                }
            }else{
                if(wdotn < 0)        
                    return null; //the line is trivialCyrusBeckAlgorithm.java:49ly invisible or an invisible point
                }
        }
        if(tl <= tu){ //check if line is in fact invisible
            //Draw line segment from p(tl)to p(tu)
            System.out.println("Lower value:"+tl+","+"Upper range:"+tu);
            MyPoint temp = cal_p_of_u(P1,P2,tl);
            MyPoint temp2 = cal_p_of_u(P1,P2,tu);
            MyPoint[] res = new MyPoint[2];
            res[0]=temp;
            res[1]=temp2;
            return res;
        }
        count++;
        System.out.println(count);
        return null;
    }
    
    private double dotProduct1(MyPoint vector1,MyPoint vector2[],int i){
        //Vector1 is the first vector with components x and y
        //Vector2 is the second vector with components x and y
        return vector1.x*vector2[i].x + vector1.y*vector2[i].y;
    }
    private double dotProduct2(MyPoint[] vector1,MyPoint vector2[],int i){
        //Vector1 is the first vector with components x and y
        //Vector2 is the second vector with components x and y
        return vector1[i].x*vector2[i].x + vector1[i].y*vector2[i].y;
    }
    
    private MyPoint cal_p_of_u(MyPoint p1, MyPoint p2,double u){ //P(u) = P1 +(P2-P1)u;
        MyPoint temp = new MyPoint();
        temp.x = p1.x + (p2.x - p1.x)*u;
        temp.y = p1.y + (p2.y - p1.y)*u;
        return temp;
    }
    public void init(GLAutoDrawable glad) {
       final GL2 gl=glad.getGL().getGL2();
       final GLU glu=new GLU();
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glViewport(0,0,640,480);
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(0, 640, 0, 480);
    }

    public void display(GLAutoDrawable glad) {
        final GL2 gl=glad.getGL().getGL2();
        final GLU glu=new GLU();
        final GLUT glut = new GLUT();
        gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
        gl.glColor3f(0.0f,1.0f,1.0f);
        
        lineDDA(gl,250,100,350,100);
        lineDDA(gl,350,100,450,150);
        lineDDA(gl,450,150,450,250);
        gl.glColor3f(1.0f, 0.5f, 0.0f);//orange
        lineDDA(gl,450,250,350,300);
        gl.glColor3f(0.0f,1.0f,1.0f);
        lineDDA(gl,350,300,250,300);
        lineDDA(gl,250,300,150,250);
        gl.glColor3f(1.0f, 0.5f, 0.0f);//orange
        lineDDA(gl,150,250,150,150);
        gl.glColor3f(0.0f,1.0f,1.0f);
        lineDDA(gl,150,150,250,100);
        
        gl.glColor3f(0.0f,1.0f, 0.0f);
        lineDDA(gl,50,150,500,350); //Line to be clipped
        
        MyPoint n[]=new MyPoint[8];
        n[0]=new MyPoint(1,1);
        n[1]=new MyPoint(1,0);
        n[2]=new MyPoint(1,-1);
        n[3]=new MyPoint(0,-1);
        n[4]=new MyPoint(-1,-1);
        n[5]=new MyPoint(-1,0);
        n[6]=new MyPoint(-1,1);
        n[7]=new MyPoint(0,1);
        MyPoint f[]=new MyPoint[8];
        f[0]=new MyPoint(250,100);
        f[1]=new MyPoint(150,250);
        f[2]=new MyPoint(150,250);
        f[3]=new MyPoint(350,300);
        f[4]=new MyPoint(350,300);
        f[5]=new MyPoint(450,150);
        f[6]=new MyPoint(450,150);
        f[7]=new MyPoint(250,100);
        MyPoint[] res = new MyPoint[2];
        x = x +1;
        if(x>3000){
            res = cb(P1,P2,n,f,8);
            draw_rect(gl,P1.x-3,P1.y-3,res[0].x-5,res[0].y-2);
        }
        if(x>2000){
            res = cb(P1,P2,n,f,8);
            draw_rect(gl,P2.x+5,P2.y+5,res[1].x+2,res[1].y+5);
        }
    }
     public void draw_rect(GL2 gl,double x1,double y1,double x2,double y2){
        gl.glPointSize(0);
        gl.glColor3f(0.0f,0.0f,0.0f);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2d(x1-2,y1-2);
        gl.glVertex2d(x2,y1-2);
        gl.glVertex2d(x2,y2);
        gl.glVertex2d(x1-2,y2);
        gl.glEnd();
    }
     
    public void lineDDA(GL2 gl,double x1,double y1,double x2,double y2) {
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
    private void setpixel(GL2 gl,double x,double y){
        gl.glPointSize(3);
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2d(x, y);
        gl.glEnd( );
    }
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
    public void dispose(GLAutoDrawable glad) {
       
    }    
}

class MyPoint{
    double x;
    double y;
    MyPoint(){}
    MyPoint(double x,double y){
        this.x=x;
        this.y=y;
    }
}
   