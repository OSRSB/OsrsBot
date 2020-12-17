package rsb.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Copyright 1999-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class LogOutputStream extends OutputStream {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Used to maintain the contract of {@link #close()}.
	 */
	protected boolean hasBeenClosed = false;

	/**
	 * The internal buffer where data is stored.
	 */

	protected ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_LENGTH);

	/**
	 * The default number of bytes in the buffer. =2048
	 */
	public static final int DEFAULT_BUFFER_LENGTH = 2048;

	/**
	 * The category to write to.
	 */
	protected Logger category;

	/**
	 * The priority to use when writing to the Category.
	 */
	protected Level priority;

	/**
	 * Creates the LogOutputStream to flush to the given Category.
	 *
	 * @param cat      the Category to write to
	 * @param priority the Priority to use when writing to the Category
	 * @throws IllegalArgumentException if cat == null or priority == null
	 */
	public LogOutputStream(final Logger cat, final Level priority) throws IllegalArgumentException {
		if (cat == null) {
			throw new IllegalArgumentException("cat == null");
		}
		if (priority == null) {
			throw new IllegalArgumentException("priority == null");
		}

		this.priority = priority;
		category = cat;
	}

	/**
	 * Closes this output stream and releases any system resources associated
	 * with this stream. The general contract of <code>close</code> is that it
	 * closes the output stream. A closed stream cannot perform output
	 * operations and cannot be reopened.
	 */

	@Override
	public void close() {
		flush();
		hasBeenClosed = true;
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be
	 * written out. The general contract of <code>flush</code> is that calling
	 * it is an indication that, if any bytes previously written have been
	 * buffered by the implementation of the output stream, such bytes should
	 * immediately be written to their intended destination.
	 */
	@Override
	public void flush() {
		int pos = buffer.position();
		if (pos == 0) {
			return;
		}
		// don't print out blank lines; flushing from PrintStream puts out these
		if (pos == LogOutputStream.LINE_SEPARATOR.length()) {

			if (((char) buffer.get(0) == LogOutputStream.LINE_SEPARATOR.charAt(0)) && ((pos == 1) || // <-
					// Unix
					// &
					// Mac,
					// ->
					// Windows
					((pos == 2) && ((char) buffer.get(1) == LogOutputStream.LINE_SEPARATOR.charAt(1))))) {
				reset();
				return;
			}
		}

		category.log(priority, new String(buffer.array()));
		reset();
	}

	private void reset() {
		buffer.clear();
	}

	/**
	 * Writes the specified byte to this output stream. The general contract for
	 * <code>write</code> is that one byte is written to the output stream. The
	 * byte to be written is the eight low-order bits of the argument
	 * <code>b</code>. The 24 high-order bits of <code>b</code> are ignored.
	 *
	 * @param b the <code>byte</code> to write
	 * @throws IOException if an I/O error occurs. In particular, an
	 *                     <code>IOException</code> may be thrown if the output stream
	 *                     has been closed.
	 */
	@Override
	public void write(final int b) throws IOException {
		if (hasBeenClosed) {
			throw new IOException("The stream has been closed.");
		}

		// don't log nulls
		if (b == 0) {
			return;
		}

		if (buffer.position() >= buffer.capacity()) {
			//make bigger
			ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
			newBuffer.put(buffer);
			buffer = newBuffer;
		}

		buffer.put((byte) b);
	}
}