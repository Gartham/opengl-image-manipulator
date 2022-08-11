package com.gartham.tools.opengl.imagemanipulator;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.alixia.javalibrary.JavaTools;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public final class ImageManipulatorEventListener implements GLEventListener {

	private int prog;

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

		int vbo;
		{
			IntBuffer b = IntBuffer.allocate(1);
			gl.glGenBuffers(1, b);
			vbo = b.get(0);
		}
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo);
		FloatBuffer triangles = FloatBuffer.wrap(new float[] { 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1 });
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
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
		// Draw code goes here.
	}
}