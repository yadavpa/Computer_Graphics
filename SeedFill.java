//Simple seed fill algorithm for 4-connected boundary-defined regions
//Seed(x,y) is seed pixel
package org.yourorghere;

import java.util.Stack;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

public class SeedFill {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        final GLCanvas canvas=new GLCanvas(cap);
        Rend r=new Rend(); 
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("SeedFill Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}

class Rend implements GLEventListener{
    Stack<Pixel> s=new Stack<Pixel>();
    int[][] color=new int[640][480];
    int stack_count=0;
    private void seedFill(GL gl,int x,int y){
        int x1,y1;
        gl.glBegin(GL.GL_POINTS);
        Pixel top=null;
        s.push(new Pixel(x,y));
        stack_count++;
        while(!s.isEmpty()){
            top=s.peek();
            s.pop();
            stack_count--;
            x1=top.x;
            y1=top.y;
            gl.glVertex2i(x1,y1);
            color[x1][y1]=2;//pixels colored are set to 2 in color array
            if(color[x1+1][y1]!=1 && color[x1+1][y1]!=2){//checking if a pixel is not boundary and not colored
                s.push(new Pixel(x1+1,y1));stack_count++;}
            if(color[x1][y1+1]!=1 && color[x1][y1+1]!=2){
                s.push(new Pixel(x1,y1+1));stack_count++;}
            if(color[x1-1][y1]!=1 && color[x1-1][y1]!=2){
                s.push(new Pixel(x1-1,y1));stack_count++;}
            if(color[x1][y1-1]!=1 && color[x1][y1-1]!=2){
                s.push(new Pixel(x1,y1-1));stack_count++;}
            System.out.println(stack_count);
        }
       gl.glEnd();
        System.out.println(stack_count);
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
        gl.glColor3f(0.0f,1.0f,0.0f);
        lineBres(gl,100,100,200,100);
        lineBres(gl,200,100,200,200);
        lineBres(gl,200,200,100,200);
        lineBres(gl,100,200,100,100);
        seedFill(gl,150,150) ;
    }
    private void lineBres(GL gl,int x1,int y1,int x2,int y2){
        int dx= Math.abs(x2-x1);
        int dy= Math.abs(y2-y1);
        int temp,interchange,e;
        int s1= (int)Math.signum(x2-x1);
        int s2= (int)Math.signum(y2-y1);
        if(dy > dx){
            temp=dx;
            dx=dy;
            dy=temp;
            interchange=1;           
        }else{
            interchange=0;
        }
        e= 2*dy-dx;
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
    
    private void setpixel(GL gl,int x,int y){
        color[x][y]=1;
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2i (x, y);
        gl.glEnd( );
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
      
    }   
}

class Color{
    float r,g,b;
    Color(float r ,float g,float b){
        this.r=r;
        this.g=g;
        this.b=b;
    }
}

class Pixel{
    int x,y;
    Pixel(int x,int y){
        this.x=x;
        this.y=y;
    }
}
//        float[] arr1={1.0f,0.0f,0.0f};
//	FloatBuffer newColor = FloatBuffer.wrap(arr1);
//        float[] arr2={0.0f,0.0f,0.0f};
//	FloatBuffer bColor = FloatBuffer.wrap(arr2);
//        FloatBuffer f=FloatBuffer.allocate(3);
//        float[] arr=new float[3];
//        for(int i=0;i<640;i++){
//            for(int j=0;j<480;j++){
//                gl.glReadPixels(i,j,1,1,GL.GL_RGB,GL.GL_FLOAT,f);
//                if(f.equals(newColor))
//                    color[i][j]=1;
//                else if(f.equals(bColor))
//                    color[i][j]=2;
//            }
//        }