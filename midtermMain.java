//
//  midtermMain.java
//
//  Main class for CG 2D Pipeline midterm
//
//  ****** NOT TO BE MODIFIED BY STUDENTS ********
//
//  Created by Joe Geigel on 1/12/12.
//  Copyright 2011 Rochester institute of Technology. All rights reserved.
//

import java.awt.*;
import java.awt.event.*;

public class midtermMain implements KeyListener, MouseListener {

    private cgCanvas C;
    private int displayNumber = 1;

    private int triangle;
    private int square;
    private int octagon;
    private int star;

    private static int drawHeight = 500;
    private static int drawWidth = 500;

    midtermMain (int w, int h)
    {
        // define your canvas
        C = new cgCanvas (w, h);

        // load all of your polygons
        float x[] = new float [10];
        float y[] = new float [10];

        // triangle
        x[0] = 25.0f; y[0] = 125.0f;
        x[1] = 75.0f; y[1] = 125.0f;
        x[2] = 50.0f; y[2] = 175.0f;
        triangle = C.addPoly (x, y, 3);

        // square
        x[0] = 125.0f; y[0] = 125.0f;
        x[1] = 175.0f; y[1] = 125.0f;
        x[2] = 175.0f; y[2] = 175.0f;
        x[3] = 125.0f; y[3] = 175.0f;
        square = C.addPoly (x, y, 4);

        // octagon
        x[0] = 25.0f; y[0] = 25.0f;
        x[1] = 35.0f; y[1] = 15.0f;
        x[2] = 55.0f; y[2] = 15.0f;
        x[3] = 75.0f; y[3] = 25.0f;
        x[4] = 75.0f; y[4] = 55.0f;
        x[5] = 55.0f; y[5] = 75.0f;
        x[6] = 35.0f; y[6] = 75.0f;
        x[7] = 25.0f; y[7] = 55.0f;
        octagon = C.addPoly (x, y, 8);

        // star
        x[0] = 150.0f; y[0] = 90.0f;
        x[1] = 140.0f; y[1] = 65.0f;
        x[2] = 110.0f; y[2] = 65.0f;
        x[3] = 140.0f; y[3] = 40.0f;
        x[4] = 110.0f; y[4] = 10.0f;
        x[5] = 150.0f; y[5] = 25.0f;
        x[6] = 190.0f; y[6] = 10.0f;
        x[7] = 160.0f; y[7] = 40.0f;
        x[8] = 190.0f; y[8] = 65.0f;
        x[9] = 160.0f; y[9] = 65.0f;
        star = C.addPoly (x, y, 10);

    }


    // Because we are a KeyListener
    public void keyTyped(KeyEvent e)
    {
        // What key did we type?
        char key = e.getKeyChar();

        if ((key == 'C') || (key == 'c')) displayNumber = 2; // clip
        if ((key == 'P') || (key == 'p')) displayNumber = 1; // polygon
        if ((key == 'T') || (key == 't')) displayNumber = 3; // transform
        if ((key == 'V') || (key == 'v')) displayNumber = 0; // viewport

        if ((key == 'Q') || (key == 'q')) System.exit(0); // quit


        doDraw();
    }
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}


    // Because we are a MouseListener
    public void mouseClicked(MouseEvent e)
    {
        displayNumber++;
        doDraw();
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    /*
     * Function that draws the entire contents of the modeled world.
     */

    private void drawPolysNorm(  ) {

        /*
         * Draw a red triangle
         */
        C.clearTransform();
        C.setColor( 1.0f, 0.0f, 0.0f );
        C.drawPoly (triangle);

        /*
         * Draw a white square
         */
        C.setColor( 1.0f, 1.0f, 1.0f );
        C.drawPoly (square);

        /*
         * Draw a blue otcagon
         */
        C.setColor( 0.0f, 0.0f, 1.0f );
        C.drawPoly (octagon);

        /*
         * Draw a green star
         */
        C.setColor( 0.0f, 1.0f, 0.0f );
        C.drawPoly (star);
    }

    /**
     * Draw the world transformed...used for transformation tests.
     */
    private void drawPolysXform(  ) {

        /*
         * Draw a red triangle rotated
         */
        C.clearTransform();
        C.rotate (-25.0f);
        C.setColor( 1.0f, 0.0f, 0.0f );
        C.drawPoly (triangle);

        /*
         * Draw a white square translated
         */
        C.clearTransform();
        C.translate (80.0f, 75.0f);
        C.setColor( 1.0f, 1.0f, 1.0f );
        C.drawPoly (square);

        /*
         * Draw a blue octagon scaled
         */
        C.clearTransform();
        C.scale (0.75f, 0.5f);
        C.setColor( 0.0f, 0.0f, 1.0f );
        C.drawPoly (octagon);

        /*
         * Draw a green star translated, scaled, rotated, then scaled back
         */
        C.clearTransform();
        C.translate (50.0f, 50.0f);
        C.scale (2.0f, 2.0f);
        C.rotate (-10.0f);
        C.translate (-50.0f, 50.0f);
        C.setColor( 0.0f, 1.0f, 0.0f );
        C.drawPoly (star);
    }

    /*
     * The display function
     */
    public void doDraw()
    {
        /*
         * Set clear color to gray
         */
        C.setColor ( 0.8f, 0.8f, 0.8f );
        C.clear();

        /*
         * plain old polygon test
         */
        if ( (displayNumber % 4) == 1) {

            /* default clipping */
            C.setClipWindow( 0.0f, 500.0f, 0.0f, 500.0f );

            /* default viewport */
            C.setViewport( 0, 0, drawWidth, drawHeight );

            /* draw the polys */
            drawPolysNorm();
        }
        else if ( (displayNumber % 4) == 2) {


            /* clipping test */
            C.setClipWindow( 35.0f, 175.0f, 35.0f, 165.0f );

            /* default viewport */
            C.setViewport( 0, 0, drawWidth, drawHeight );

            /* draw the polys */
            drawPolysNorm();

        }
        else if ( (displayNumber % 4) == 3) {

            /* default clipping */
            C.setClipWindow( 0.0f, 500.0f, 0.0f, 500.0f );

            /* default viewport */
            C.setViewport( 0, 0, drawWidth, drawHeight );

            /* draw the tranformed polys */
            drawPolysXform();
        }

        else /* displayNumber == 0 */ {

            /* default clipping */
            C.setClipWindow( 0.0f, 500.0f, 0.0f, 500.0f );

            /* have some fun with the view port */
            int wdiff = drawWidth / 5;
            int hdiff = drawHeight / 5;
            int x = 0;
            int y = 0;
            int i,j;
            for (i=0; i < 5; i++) {
                C.setViewport (x, y, wdiff, hdiff);
                drawPolysNorm();
                y+= hdiff;
                x+= wdiff;
            }
        }

        /*
         * Initiate a redraw
         */
        C.repaint();

    }

    static public void main(String[] args)
    {

        midtermMain M = new midtermMain (drawWidth, drawHeight);
        M.C.addKeyListener (M);
        M.C.addMouseListener (M);
        M.doDraw();


        Frame f = new Frame( "CG Midterm" );
        f.add("Center", M.C);
        f.pack();
        f.setResizable (false);
        f.setVisible(true);

    }

}
