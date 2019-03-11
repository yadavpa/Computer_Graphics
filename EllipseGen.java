//Efficient midpoint ellipse algorithm for the first quadrant
package org.yourorghere;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class EllipseGen {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        GLCanvas canvas=new GLCanvas(cap);
        ERenderer er=new ERenderer();
        canvas.addGLEventListener(er);
        JFrame frame=new JFrame("Ellipse Algorithm");
        frame.add(canvas);
        frame.setSize(480,480);
        frame.setVisible(true);
    }    
}

class ERenderer implements GLEventListener{
    
    public void drawEllipse(GL gl,int a,int b){
        int x=Math.round(a+1/2);
        int y=0;
        int taa= a*a;
        int t2aa= 2 * taa;
        int t4aa= 2 * t2aa;
        int tbb= b*b;
        int t2bb= 2 * tbb;
        int t4bb= 2 * t2bb;
        int t2abb= a* t2bb;
        int t2bbx=t2bb*x;
        int tx=x;
        int d1=t2bbx*(x-1)+tbb/2 + t2aa*(1-tbb);
        while(t2bb*tx > t2aa*y){//starts in region 1
            setpixel(gl,x,y);
            if(d1<0){
                y=y+1; //move vertically
                d1=d1+t4aa*y+t2aa;
                tx=x-1;
            }else{
                x=x-1;//move diagonally
                y=y+1;
                d1=d1-t4bb*x + t4aa*y +t2aa;
                tx=x;
            }
        }
        int d2= t2bb*(x*x+1)-t4bb*x +t2aa*(y*y+y-tbb)+taa/2;
        while(x>=0){
            setpixel(gl,x,y);
            if(d2<0){
                x=x-1;//move diagonally
                y=y+1;
                d2=d2+t4aa*y-t4bb*x+t2bb;
            }else{
                x=x-1;//move horizontally
                d2=d2-t4bb*x + t2bb;
            }
        }
        
    }
    public void setpixel(GL gl,int x,int y){
        gl.glPointSize(2.0f);
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2i (x, y);
        //gl.glVertex2i (y, x); //horizontal ellipse in this case
        gl.glVertex2i (-x, y);
        //gl.glVertex2i(-y, x);
        gl.glVertex2i(-x, -y);
        //gl.glVertex2i(-y, -x);
        gl.glVertex2i(x, -y);
        //gl.glVertex2i(y, -x);
        gl.glEnd( );
    }
    @Override
    public void init(GLAutoDrawable drawable) {
        final GL gl = drawable.getGL();
        final GLU glu = new GLU();
        gl.glViewport(-250, -150, 250, 150);
        gl.glMatrixMode (GL.GL_PROJECTION); 
        gl.glClearColor (1.0f, 1.0f, 1.0f, 0.0f);    //set background to white
        glu.gluOrtho2D (-150.0, 150.0, -150.0, 150.0);  // define drawing area
    }

    @Override
    public void display(GLAutoDrawable drawable) {
      final GL gl = drawable.getGL();
       gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
       gl.glColor3f (0.0f, 0.0f, 0.75f); // Set line segment color to blue.
       gl.glMatrixMode (GL.GL_MODELVIEW);
       gl.glLoadIdentity();
       drawEllipse(gl,30,50);
       gl.glTranslated(90.0f,0.0f,0.0f);
       drawEllipse(gl,50,30);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
      
    }
    
}
