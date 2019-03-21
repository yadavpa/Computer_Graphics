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
import static com.jogamp.opengl.util.gl2.GLUT.BITMAP_HELVETICA_18;
import java.util.Arrays;
import javax.swing.JFrame;

public class CohenSutherlandAlgorithm{
    public static void main(String[] args) {
        GLProfile p= GLProfile.get(GLProfile.GL2);
        GLCapabilities cap=new GLCapabilities(p);
        final GLCanvas canvas=new GLCanvas(cap);
        Renderer r=new Renderer();
        canvas.addGLEventListener(r);
        final JFrame frame=new JFrame("Cohen Sutherland Line Clipping Algorithm");
        frame.add(canvas);
        frame.setSize(640,480);
        frame.setVisible(true);
        final FPSAnimator animator = new FPSAnimator(canvas,400,true);
	animator.start();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class Renderer implements GLEventListener{
    public double p1x = 75,p1y = 200, p2x = 375, p2y = 400;
    public double xl=150,xr=450,yt=350,yb=100;
    private int x = 0;
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
    //Implementing Cohen Sutherland algorithm. Clipping a line from P1 = (x1,y1) to P2 (x2,y2)
    public double[] cohenSutherlandClip(GL2 gl,double x1, double y1,double x2,double y2){
        int p1code[]=new int[4];
        int p2code[]=new int[4];
        double res[]=new double[4];
        boolean accept=false;
        p1code= computeCode(x1,y1,xl,xr,yt,yb);
        p2code= computeCode(x2,y2,xl,xr,yt,yb);
        gl.glColor3f(1.0f, 0.5f, 0.0f);//orange
        String s1 = "P1: ["+p1code[0]+" "+p1code[1]+" "+p1code[2]+" "+p1code[3]+"]";
        drawString(gl,BITMAP_HELVETICA_18,s1.toCharArray(),130,450);
        String s2 = "P2: ["+p2code[0]+" "+p2code[1]+" "+p2code[2]+" "+p2code[3]+"]";
        drawString(gl,BITMAP_HELVETICA_18,s2.toCharArray(),370,450);
//        for(int i=0;i<4;i++){
//            System.out.println(p1code[i]+" "+p2code[i]);
//        }
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
                if(code_out[2]==1){//point is to the right of the clip rectangle
                    y= m*(xr - x1)+y1;
                    x= xr;
                }
                if(code_out[1]==1){ //point is at the bottom of clip rectangle
                    x= (1/m)*(yb - y1) + x1;
                    y=yb;
                }
                if(code_out[0]==1){//point is at the top of clip rectangle
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
            if(x==2001){
                System.out.println("Line accepted from ("+x1+","+y1+") to ("+x2+","+y2+")");
            }
            res[0]=x1;res[1]=y1;res[2]=x2;res[3]=y2;
        }
        else{
            System.out.println("Line rejected");
        }
       return res;
    }
    
    public void init(GLAutoDrawable drawable) {
        final GL2 gl=drawable.getGL().getGL2();
        final GLU glu=new GLU();
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        gl.glViewport(0,0,640,480);
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluOrtho2D(0, 640, 0, 480);
    }

    public void display(GLAutoDrawable drawable) {
        final GL2 gl=drawable.getGL().getGL2();
        final GLU glu=new GLU();
        final GLUT glut = new GLUT();
        gl.glClear (GL.GL_COLOR_BUFFER_BIT);  // Set display window to color.
        gl.glColor3f(0.0f,1.0f,1.0f);
        
        lineDDA(gl,xl,yb,xr,yb);
        lineDDA(gl,xr,yb,xr,yt);
        gl.glColor3f(1.0f, 0.5f, 0.0f);//orange
        lineDDA(gl,xr,yt,xl,yt);
        lineDDA(gl,xl,yt,xl,yb);
        
        gl.glColor3f(0.0f,1.0f, 0.0f);
        lineDDA(gl,p1x,p1y,p2x,p2y); //Line to be clipped
        setDot(gl,p1x, p1y);
        setDot(gl,p2x,p2y);
        gl.glColor3f(0.1f,0.1f,0.1f);
        lineDDA(gl,xl,yb,xl,yb-90);lineDDA(gl,xl,yb,xl-100,yb);
        lineDDA(gl,xr,yb,xr,yb-90);lineDDA(gl,xr,yb,xr+100,yb);
        lineDDA(gl,xl,yt,xl,yt+90);lineDDA(gl,xl,yt,xl-100,yt);
        lineDDA(gl,xr,yt,xr,yt+90);lineDDA(gl,xr,yt,xr+100,yt);
        drawString(gl,BITMAP_HELVETICA_18,"0000".toCharArray(),265,220);//centre
        drawString(gl,BITMAP_HELVETICA_18,"0001".toCharArray(),35,220); //left
        drawString(gl,BITMAP_HELVETICA_18,"0010".toCharArray(),500,220);//right
        drawString(gl,BITMAP_HELVETICA_18,"1000".toCharArray(),265,375);//top
        drawString(gl,BITMAP_HELVETICA_18,"0100".toCharArray(),265,50);//bottom

        drawString(gl,BITMAP_HELVETICA_18,"0001".toCharArray(),35,50); //bottom left
        drawString(gl,BITMAP_HELVETICA_18,"0010".toCharArray(),500,375);//top right
        drawString(gl,BITMAP_HELVETICA_18,"1000".toCharArray(),35,375);//top left 510
        drawString(gl,BITMAP_HELVETICA_18,"0100".toCharArray(),500,50);//bottom right
        cohenSutherlandClip(gl,p1x,p1y,p2x,p2y);
        x = x + 1;
        if(x>3000){
            double res[]=cohenSutherlandClip(gl,p1x,p1y,p2x,p2y);
            draw_rect(gl,res[2]+5,res[3]+5,p2x+5,p2y+5);
        }
        if(x>2000){
            double res[]=cohenSutherlandClip(gl,p1x,p1y,p2x,p2y);
            draw_rect(gl,p1x,p1y,res[0],res[1]);
        }
//        gl.glColor3f(0.0f,1.0f,1.0f);
        //lineDDA(gl,res[0],res[1],res[2],res[3]);
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
    
    private void setDot(GL2 gl,double x,double y){
        gl.glPointSize(3);
        gl.glColor3f(1.0f,1.0f,1.0f);
        gl.glBegin (GL.GL_POINTS);
        gl.glVertex2d(x, y);
        gl.glEnd( );
    }
    private void drawString(GL2 gl,int font_type,char s[],double x,double y){
        GLUT glut=new GLUT();
        gl.glRasterPos2d(x, y);
        for(int i=0;i< s.length;i++){
            glut.glutBitmapCharacter(font_type,s[i]);
        }
    }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
       
    }

    public void dispose(GLAutoDrawable glad) {
     
    }
    
}

