/*
  Copyright (c) 2011 Timothy Lovorn

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
i,  -- ^^^ MIT license
  -- quoted sections in comments are from Java 1.6 API
*/

package javaAnimTest;
import javaAnimTest.*;

import java.applet.*;
import java.awt.*;
import java.net.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public class Animate extends Applet implements Runnable {
    final int fps = 24;
    Thread animator = null;
    ArrayList<Image> imageList;
    int frameNumber = 0, totalFrames, waitTime;
    
    // constructor for applet
    public void init() {
	loadImages("rickroll", "png", 24);
    }

    // executes after init() and also "each time the applet is revisited"
    public void start() {
        animator = new Thread(this);
        animator.start();
    }

    // executes "when the Web page that contains this applet has been replaced
    // by another page, and also just before the applet is to be destroyed."
    public void stop() {
	System.out.printf("stopping\n");
        animator = null;
    }

    // main thread (required by Runnable)
    public void run() {
        long time = System.currentTimeMillis();
        while (Thread.currentThread() == animator) {
            // paint frame
            repaint();
            // don't go to next frame immediately
            try {
                time += waitTime;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
            } catch (InterruptedException e) { 
		System.out.printf("breaking loop\n");
                break;
            }
            // advance the frame number, roll over to 0 at numFrames
            frameNumber = (frameNumber + 1) % totalFrames;
        }     

    }

    public void update(Graphics g) {
        Dimension dims = getSize();
        Image img = imageList.get(frameNumber);
        g.drawImage(img, 0, 0, this);
    }

    // load specified series of images into imageList
    private void loadImages(String prefix, String extension, int numToLoad) {
        imageList = new ArrayList<Image>();
        for (int index = 0; index < numToLoad; index++) {
	    String fileName = "javaAnimTest\\" + prefix + "\\" + prefix + String.valueOf(index) + "." + extension;
	    File f = new File(fileName);
	    if (!f.exists())
	    {
		System.out.printf("%s does not exists!!!\n", fileName);
		System.out.printf("path is %s\n", f.getPath());
	    }
	    Image img = null;
	    try {
		img = ImageIO.read(f);
	    } 
	    catch (IOException e) {
		    System.out.printf("Caught an error\n");
	    }
            imageList.add(img);
        }
        totalFrames = numToLoad;
        waitTime = 100 * totalFrames / fps;
    }
}
