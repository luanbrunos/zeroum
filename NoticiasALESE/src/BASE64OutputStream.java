import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;


public class BASE64OutputStream extends OutputStream {

    /**
     *  If <code>true</code> then the output stream has been closed.
     */
    private boolean closed = false;

    /**
     *  The output writer.
     */
    private Writer sendTo = null;

    /**
     *  Column width to breakup out.
     */
    private final int columnWidth;

    /**
     *  Current output column.
     */
    private int column = 0;

    /**
     *  Buffer for incomplete characters.
     */
    private byte [] buffer = new byte[] { 0, 0, 0 };

    /**
     *  The number of characters currently in the buffer.
     */
    private byte inBuffer = 0;

    /**
     * Construct a BASE64 Output Stream.
     *
     * @param sendTo The text Writer to which the BASE64 output will be
     * written.
     */
    public BASE64OutputStream(Writer sendTo) {
        this(sendTo, -1);
    }

    /**
     * Construct a BASE64 Output Stream. The output will be broken into lines
     * <code>columnWidth</code> long.
     *
     * @param sendTo The text Writer to which the BASE64 output will be
     * written.
     * @param columnWidth The width of lines to break output into.
     */
    public BASE64OutputStream(Writer sendTo, int columnWidth) {
        this.sendTo = sendTo;
        this.columnWidth = columnWidth;
    }

    /**
     *  {@inheritDoc}
     */
    public void write(int b) throws IOException {
        if(closed) {
            throw new IOException("OutputStream closed.");
        }

        buffer[ inBuffer++ ] = (byte) b;

        if(buffer.length == inBuffer) {
            writeBuffer();
        }
    }

    /**
     *  {@inheritDoc}
     *
     *  <p/>The output writer is <b>NOT</b> closed.
     */
    public void close() throws IOException {
        flush();

        closed = true;
        sendTo = null;
    }

    /**
     *  {@inheritDoc}
     */
    public void flush() throws IOException {
        writeBuffer();
    }

    /**
     *  Write a full or partial buffer to the output writer.
     */
    private void writeBuffer() throws IOException {
        if(0 == inBuffer) {
            return;
        }

        int val = ((buffer[0] & 0x00FF) << 16) +
                  ((buffer[1] & 0x00FF) << 8) +
                  (buffer[2] & 0x00FF);

        int c0 = (val >> 18) & 0x003F;
        int c1 = (val >> 12) & 0x003F;
        int c2 = (val >> 6) & 0x003F;
        int c3 = val & 0x003F;

        if((columnWidth > 0) && (column > columnWidth)) {
            sendTo.write('\n');
            column = 0;
        }

        sendTo.write(encodeSixBits(c0));
        sendTo.write(encodeSixBits(c1));

        if(inBuffer > 1 ) {
            sendTo.write(encodeSixBits(c2));
        } else {
            sendTo.write('=');
        }

        if(inBuffer > 2) {
            sendTo.write(encodeSixBits(c3));
        } else  {
            sendTo.write('=');
        }


        buffer[0] = 0; buffer[1] = 0; buffer[2] = 0;

        inBuffer = 0;
        column += 4;
    }

    /**
     *  BASE64 Encoding Table
     */
    static final char encode [] = {
                                      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                                      'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                      'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                                      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                                      'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                                      'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                                      'w', 'x', 'y', 'z', '0', '1', '2', '3',
                                      '4', '5', '6', '7', '8', '9', '+', '/'
                                  };

    /**
     *  Encode six bits into a character using the standard BASE64 table.
     *
     *  @param b the bits to encode. b must be >=0 and <= 63
     *  @return the appropriate character for the input value.
     */
    private static char encodeSixBits(int b) {
        char c;

        if((b < 0) || (b > 63))
            throw new IllegalArgumentException("bad encode value");
        else
            c = encode[b];

        return c;
    }
}