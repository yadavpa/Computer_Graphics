package org.yourorghere;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class Bresenham {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        GLCanvas canvas=new GLCanvas(cap);
        Rendererb rb=new Rendererb();
        canvas.addGLEventListener(rb);
        JFrame frame=new JFrame("Bresenham's Generalised Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}

class Rendererb implements GLEventListener{
    public void lineBres(GL gl,int x1,int y1,int x2,int y2){
        int dx= x2 - x1;
        int dy= y2 - y1;int temp,interchange;
        int s1= (int)Math.signum(dx);
        int s2= (int)Math.signum(dy);
        if(dy > dx){
            temp=dx;
            dx=dy;
            dy=temp;
            interchange=1;           
        }else{
            interchange=0;
        }
        int e= 2*dy-dx;
        int x=x1,y=y1;
        for(int i=1;i<=dx;i++){
            setpixel(gl,x,y);
            while(e>0){
                if(interchange==1)
                    x= x + s1;
                else
                    y= y + s2;
                e= e- 2*dx;
            }
            if(interchange==1)
                y= y + s2;
            else
                x= x + s1;
            e= e + 2*dy;
        }
    }
    
    public void setpixel(GL gl,int x,int y){
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2i (x, y);
        gl.glEnd( );
    }
    @Override
    public void init(GLAutoDrawable drawable) {
        final GL gl = drawable.getGL();
        final GLU glu = new GLU();
        gl.glMatrixMode (GL.GL_PROJECTION); 
        gl.glClearColor (1.0f, 1.0f, 1.0f, 0.0f);   //set background to white
        glu.gluOrtho2D (0.0, 200.0, 0.0, 150.0);  // define drawing area
    }

    @Override
    public void display(GLAutoDrawable drawable) {
       final GL gl = drawable.getGL();
       gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
       gl.glColor3f (0.0f, 0.0f, 0.75f); // Set line segment color to blue.
       gl.glMatrixMode (GL.GL_MODELVIEW);
       gl.glLoadIdentity();
       lineBres(gl, 10,25, 180, 100);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
       
    }
    
}