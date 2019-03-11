//Bresenham's Circle Generation Algorithm for first quadrant
package org.yourorghere;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class CircleGen {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        GLCanvas canvas=new GLCanvas(cap);
        Rendererc rc=new Rendererc();
        canvas.addGLEventListener(rc);
        JFrame frame=new JFrame("Bresenham's Circle Drawing Algorithm");
        frame.add(canvas);
        frame.setSize(480,480);
        frame.setVisible(true);
    }    
}

class Rendererc implements GLEventListener{
    int x_i,y_i,delta_i;
    public void drawCircle(GL gl,int r){
        x_i=0;y_i=r;
        delta_i= 2*(1-r);
        int limit=0,del_a,del_b;
        while(y_i >= limit){
            setpixel(gl,x_i,y_i);
            if(delta_i < 0){ //then diagonal point is inside the actual circle
               del_a= 2*delta_i + 2*y_i -1; //(dist.frm actual circle to pixel at mH)^2-(dist.frm actual circle to pixel at mD)^2
               if(del_a <=0) //choose mH at(x_i + 1,y_i)
                  mh();
               else
                  md();
            }
            else if(delta_i > 0){//then diagonal point is outside the actual circle
                del_b= 2*delta_i - 2*x_i -1; //(dist.from ac.circle to pixel at mD)^2-(dist.from ac.circle to pixel at mV)^2
                if(del_b <=0) //choose mD at (x_i +1,y_i -1)
                  md();
                else
                  mv();
            }
            else if(delta_i ==0)
                md();
            else{}
        }
    }
    
    
    public void setpixel(GL gl,int x,int y){
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2i (x, y);
        gl.glVertex2i (y, x);
        gl.glVertex2i (-x, y);
        gl.glVertex2i(-y, x);
        gl.glVertex2i(-x, -y);
        gl.glVertex2i(-y, -x);
        gl.glVertex2i(x, -y);
        gl.glVertex2i(y, -x);
        gl.glEnd( );
    }
    public void mh(){ //move horizontally
        //System.out.println(x_i+"\t"+y_i);
        x_i= x_i+1;
        delta_i=delta_i + 2*x_i +1;
    }

    public void md(){ //move diagonally
        x_i = x_i+1;
        y_i = y_i-1;
        delta_i= delta_i + 2*x_i - 2*y_i + 2;
    }

   public void mv(){ //move vertically
        y_i = y_i -1;
        delta_i = delta_i - 2*y_i +1;
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
       drawCircle(gl,140);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
       
    }
    
}

         

