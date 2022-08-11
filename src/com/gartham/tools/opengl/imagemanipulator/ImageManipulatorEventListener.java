package com.gartham.tools.opengl.imagemanipulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.alixia.javalibrary.JavaTools;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public final class ImageManipulatorEventListener implements GLEventListener {

	private static GLU glu;

	private int prog;

	private static InputStream input = ImageManipulatorEventListener.class.getResourceAsStream("gartham.png");

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		drawable.getGL().glViewport(0, 0, width, height);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL4 gl = drawable.getGL().getGL4();

		// Create shaders
		int vertshader = gl.glCreateShader(GL4.GL_VERTEX_SHADER),
				fragshader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);

		gl.glShaderSource(vertshader, 1,
				new String[] { JavaTools.readText(getClass().getResourceAsStream("vertexShader.vs")) }, null);
		gl.glCompileShader(vertshader);
		gl.glShaderSource(fragshader, 1,
				new String[] { JavaTools.readText(getClass().getResourceAsStream("fragmentShader.fs")) }, null);
		gl.glCompileShader(fragshader);

		// Create GPU program
		prog = gl.glCreateProgram();
		gl.glAttachShader(prog, vertshader);
		gl.glAttachShader(prog, fragshader);
		gl.glLinkProgram(prog);

		gl.glUseProgram(prog);

		ByteBuffer bb = ByteBuffer.allocate(100000);
		gl.glGetShaderInfoLog(vertshader, 100000, null, bb);
		for (int i; (i = bb.get()) != 0;)
			System.out.print((char) i);

		bb = ByteBuffer.allocate(100000);
		gl.glGetProgramInfoLog(prog, 100000, null, bb);
		for (int i; (i = bb.get()) != 0;)
			System.out.print((char) i);

		int vbo;
		{
			IntBuffer b = IntBuffer.allocate(1);
			gl.glGenBuffers(1, b);
			vbo = b.get(0);
		}
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
		FloatBuffer triangles = FloatBuffer.wrap(new float[] { -1, -1, -1, 1, 1, 1, -1, -1, 1, -1, 1, 1 });
		gl.glBufferData(GL.GL_ARRAY_BUFFER, triangles.capacity() * Float.BYTES, triangles, GL.GL_STATIC_DRAW);

		int vao;
		{
			IntBuffer i = IntBuffer.allocate(1);
			gl.glGenVertexArrays(1, i);
			vao = i.get(0);
		}
		gl.glBindVertexArray(vao);
		// Pick an index.
		// The number of items per element.
		// The type of item.
		// Do you want the data to be normalized?
		// The stride, in bytes. 0 for "packed"
		// Number of bytes before starting to read this attribute.
		gl.glVertexAttribPointer(0, 2, GL.GL_FLOAT, false, 0, 0);
		// We pick index 0. That's where this attribute will go.
		// There is an X and a Y value per coord.
		// Type of value.
		// We don't want to normalize the input data.
		// The data is packed densely. Spacing of 2*sizeof(float) between each value.
		// Offset from the start of the data vector.

		gl.glEnableVertexAttribArray(0);

		// Load Texture.
//		TextureData texture;
//		try {
//			texture = TextureIO.newTextureData(GLProfile.getDefault(), input, false, "png");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(0);
//			return;
//		}
//
//		printError(gl);
//
//		// Make the Texture buffer.
//		{
//			IntBuffer i = IntBuffer.allocate(1);
//			gl.glGenTextures(1, i);
//			tb = i.get(0);
//		}
//		gl.glBindTexture(GL.GL_TEXTURE_2D, tb);
//		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, texture.getInternalFormat(), texture.getWidth(), texture.getHeight(),
//				texture.getBorder(), texture.getPixelFormat(), texture.getPixelType(), texture.getBuffer());
//		printError(gl);

		// Try loading texture using texture object.

		Texture texture;
		try {
			texture = TextureIO.newTexture(input, false, "png");
		} catch (GLException | IOException e) {
			e.printStackTrace();
			System.exit(0);
			return;
		}
		texture.bind(gl);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL4 gl = drawable.getGL().getGL4();
		gl.glClearColor(1, 0.5f, .25f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//		gl.glDrawElements(GL.GL_TRIANGLES, 2, GL.GL_FLOAT, 0);
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
		// Draw code goes here.

		printError(gl);
	}

	public static void printError(GL gl) {
		int x = gl.glGetError();
		while (x != GL.GL_NO_ERROR)
			System.out.println("Error: " + (glu == null ? glu = GLU.createGLU() : glu).gluErrorString(x));
	}
}