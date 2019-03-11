//Scan line seed fill algorithm
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

public class ScanlineSeedFill {
    public static void main(String[] args) {
        GLCapabilities cap=new GLCapabilities();
        final GLCanvas canvas=new GLCanvas(cap);
        RendS r=new RendS(); 
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("Scanline SeedFill Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}

class RendS implements GLEventListener{
    Stack<Pixelss> s=new Stack<Pixelss>();
    int[][] color=new int[640][480];
    
    private void scanline_seedfill(GL gl,int x1,int y1){
        int x,y,savex,xright,xleft,pflag,xcenter;
        gl.glBegin(GL.GL_POINTS);
        Pixelss top=null;
        s.push(new Pixelss(x1,y1));
        while(!s.isEmpty()){
            top=s.peek();
            s.pop();
            x=top.x;
            y=top.y;
            gl.glVertex2i(x,y);
            color[x][y]=2;  //pixels colored are set to 2 in color array
            savex=x;  //save the x-coordinate of the seed pixel
            x = x + 1;  //fill the span to the right of pixel
            while(color[x][y]!=1){
                gl.glVertex2i(x,y);
                color[x][y]=2;
                x = x+1;
            }
            xright = x-1;
            x=savex;
            x=x-1;
            while(color[x][y]!=1){
                gl.glVertex2i(x,y);
                color[x][y]=2;
                x = x-1;
            }
            xleft=x+1;
            x=savex;
            x = xleft;
            y = y+1;
            while(x<= xright){
                pflag=0;
                while(color[x][y]!=1 && color[x][y]!=2 && x<xright){
                    if(pflag==0){
                        pflag=1;
                    }                      
                    x=x+1;
                }
                if(pflag==1){
                    if( x == xright && color[x][y]!=1 && color[x][y]!=2)
                        s.push(new Pixelss(x,y));
                    else
                        s.push(new Pixelss(x-1,y));
                    pflag=0;
                }            
                xcenter=x;
                while((color[x][y]==1 || color[x][y]==2) && x<xright){
                    x= x+1;
                }
                if(x==xcenter)
                    x=x+1;
            }
            x=xleft;
            y=y-2;
            while(x<= xright){
                pflag=0;
                while(color[x][y]!=1 && color[x][y]!=2 && x<xright){
                    if(pflag==0){
                        pflag=1;
                    }                      
                    x=x+1;
                }
                if(pflag==1){
                    if( x == xright && color[x][y]!=1 && color[x][y]!=2)
                        s.push(new Pixelss(x,y));
                    else
                        s.push(new Pixelss(x-1,y));
                    pflag=0;
                }            
                xcenter=x;
                while((color[x][y]==1 || color[x][y]==2) && x<xright){
                    x= x+1;
                }
                if(x==xcenter)
                    x=x+1;
            }
        } 
       gl.glEnd();
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
        lineBres(gl,100,100,500,100);
        lineBres(gl,500,100,500,200);
        lineBres(gl,500,200,400,300);
        lineBres(gl,400,300,300,200);
        lineBres(gl,300,200,200,300);
        lineBres(gl,200,300,100,200);
        lineBres(gl,100,200,100,100);
//        lineBres(gl,100,100,200,100);
//        lineBres(gl,200,100,200,200);
//        lineBres(gl,200,200,100,200);
//        lineBres(gl,100,200,100,100);
        scanline_seedfill(gl,150,150);
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

class Pixelss{
    int x,y;
    Pixelss(int x,int y){
        this.x=x;
        this.y=y;
    }
}
