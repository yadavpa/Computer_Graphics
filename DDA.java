//DDA for rasterizing a line with end points (x1,y1) and (x2,y2), assumed not equal
package org.yourorghere;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class DDA {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        final GLCanvas canvas=new GLCanvas(cap);
        Renderer r=new Renderer();
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("DDA Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}

class Renderer implements GLEventListener{
    public void lineDDA(GL gl,int x1,int y1,int x2,int y2) {
        int dx= x2  -x1;
        int dy= y2 - y1;
        int steps;
        float x_increment,y_increment,x= x1,y=y1;
        if(Math.abs(dx) > Math.abs(dy))
            steps=Math.abs(dx);
        else
            steps=Math.abs(dy);
        x_increment= (float)dx/(float)steps;
        y_increment= (float)dy/(float)steps;
        setpixel(gl,Math.round(x),Math.round(y));
        for(int i=0;i<steps;i++){
            x= x+ x_increment;
            y= y+ y_increment;
            setpixel(gl,Math.round(x),Math.round(y));
          //  Thread.sleep(1000);
        }    
    }
    
    public void setpixel(GL gl,int x,int y){
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex2i(x, y);
        gl.glEnd();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL gl=drawable.getGL();
        final GLU glu=new GLU();
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glMatrixMode(GL.GL_PROJECTION);
        glu.gluOrtho2D(0,640,0,480);    
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL gl=drawable.getGL();
        gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
        gl.glColor3f (0.0f, 0.0f, 0.75f); 
      //  gl.glMatrixMode (GL.GL_MODELVIEW);
       // gl.glLoadIdentity();
        lineDDA(drawable.getGL(), -50,0, 200, 150);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
     
    }
  
}